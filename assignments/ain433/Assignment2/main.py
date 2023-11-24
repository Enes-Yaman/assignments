import math
import os
import time

import cv2
import numpy as np
from numba import njit, prange
from sklearn.svm import LinearSVC
from sklearn.utils import shuffle


@njit(parallel=True)
def hough_circles(edges, angle, min_radius, max_radius, threshold=10):
    height, width = edges.shape

    accumulator = np.zeros((height, width, max_radius + 1), dtype=np.uint8)

    for y in prange(height):
        for x in prange(width):
            if edges[y, x] == 255:
                for radius in prange(min_radius, max_radius + 1):
                    a = int(x - radius * math.cos(math.radians(angle[y][x])))
                    b = int(y - radius * math.sin(math.radians(angle[y][x])))

                    if 0 <= a < width and 0 <= b < height:
                        accumulator[b, a, radius] += 1

    circles = np.where(accumulator >= threshold)
    return circles


def merge_circles(circles, center_dist_threshold=20, radius_distance_threshold=20):
    merged_circles = [[], [], []]

    if circles[0].size > 0:
        y, x, r = circles
        remaining_indices = list(range(len(y)))

        while remaining_indices:
            current_index = remaining_indices[0]
            current_y = y[current_index]
            current_x = x[current_index]
            current_r = r[current_index]

            merged_indices = [current_index]

            for i in remaining_indices[1:]:
                y_dist = abs(y[i] - current_y)
                x_dist = abs(x[i] - current_x)
                r_dist = abs(r[i] - current_r)

                center_distance = np.sqrt(y_dist ** 2 + x_dist ** 2)

                if center_distance < center_dist_threshold and r_dist < radius_distance_threshold:
                    merged_indices.append(i)

            if len(merged_indices) > 1:
                # Merge circles if there are more than one in the group
                average_y = int(np.mean([y[i] for i in merged_indices]))
                average_x = int(np.mean([x[i] for i in merged_indices]))
                average_r = int(np.mean([r[i] for i in merged_indices]))

                merged_circles[0].append(average_y)
                merged_circles[1].append(average_x)
                merged_circles[2].append(average_r)
            else:
                # Add the single circle to the result
                merged_circles[0].append(current_y)
                merged_circles[1].append(current_x)
                merged_circles[2].append(current_r)

            # Update the remaining indices
            remaining_indices = [i for i in remaining_indices if i not in merged_indices]

    return merged_circles


def process_images(input_folder):
    images = {}
    for filename in os.listdir(input_folder):
        if filename.endswith(".jpg") or filename.endswith(".png"):
            # Read the image
            image_path = os.path.join(input_folder, filename)
            image = cv2.imread(image_path)
            h, w, _ = image.shape
            resized = cv2.resize(image, (int(512 * (w / h)), 512))
            images[filename] = resized
    return images


def detect_circles(image, center_dist_threshold, radius_distance_threshold, min_radius, max_radius):
    v = np.median(image)
    lower = int(max(0, (1.0 - .33) * v))
    upper = int(min(255, (1.0 + .33) * v))
    edges = cv2.Canny(image, lower, upper)
    gradient_x = cv2.Sobel(image, cv2.CV_64F, 1, 0, ksize=5)
    gradient_y = cv2.Sobel(image, cv2.CV_64F, 0, 1, ksize=5)
    _, angle = cv2.cartToPolar(gradient_x, gradient_y, angleInDegrees=True)

    circles = hough_circles(edges, angle, min_radius=min_radius, max_radius=max_radius)
    circles = merge_circles(circles, center_dist_threshold=center_dist_threshold,
                            radius_distance_threshold=radius_distance_threshold)
    return circles


def draw_circles(image, circles):
    im = image.copy()
    if circles is not None:
        y, x, r = circles
        circles = np.column_stack((x, y, r))

        for circle in circles:
            center = (circle[0], circle[1])
            radius = circle[2]

            cv2.circle(im, center, radius, (0, 255, 0), 2)
            cv2.circle(im, center, 1, (0, 0, 255), 1)
    return im


def write_images(image, out_folder, filename):
    if not os.path.exists(out_folder):
        os.mkdir(out_folder)
    cv2.imwrite(f'{out_folder}/{filename}', image)


def calculate_gradients(image):
    dx = cv2.Sobel(image, cv2.CV_64F, 1, 0, ksize=5)
    dy = cv2.Sobel(image, cv2.CV_64F, 0, 1, ksize=5)

    magnitude = np.sqrt(dx ** 2 + dy ** 2)
    angle = np.arctan2(dy, dx) * 180 / np.pi

    return magnitude, angle


def calculate_histogram(magnitude, angle, orientations=9, cells_per_block=(2, 2), cell_size=(8, 8)):
    # Create histograms for each cell
    hist = np.zeros((magnitude.shape[0] // cell_size[0], magnitude.shape[1] // cell_size[1], orientations))

    for i in range(orientations):
        angle_range = (i * 180 / orientations, (i + 1) * 180 / orientations)
        angle_mask = np.logical_and(angle >= angle_range[0], angle < angle_range[1])
        hist[:, :, i] = np.sum(magnitude * angle_mask, axis=(0, 1))

    # Normalize histograms in each block
    normalized_hist = np.zeros((hist.shape[0] - cells_per_block[0] + 1, hist.shape[1] - cells_per_block[1] + 1,
                                cells_per_block[0], cells_per_block[1], orientations))

    for i in range(cells_per_block[0]):
        for j in range(cells_per_block[1]):
            block_hist = hist[i:i + normalized_hist.shape[0], j:j + normalized_hist.shape[1], :]
            normalized_hist += block_hist[:, :, np.newaxis, np.newaxis, :]

    return normalized_hist.reshape(-1)


def calculate_hog(image, orientations=9, pixels_per_cell=(8, 8), cells_per_block=(2, 2), visualize=False):
    magnitude, angle = calculate_gradients(image)
    features = calculate_histogram(magnitude, angle, orientations, cells_per_block, pixels_per_cell)

    return features


def part1():
    train_images = process_images("AIN433_F23_PA2_Dataset_v1/Train")
    for train_file_name, train_image in train_images.items():
        gray = cv2.cvtColor(train_image, cv2.COLOR_BGR2GRAY)
        circles = detect_circles(gray, 100, 100, 100, 600)
        drawn_image = draw_circles(train_image, circles)
        write_images(drawn_image, 'Train_Hough', train_file_name)

    testV_images = process_images('AIN433_F23_PA2_Dataset_v1/TestV')
    for testV_file_name, testV_image in testV_images.items():
        gray = cv2.cvtColor(testV_image, cv2.COLOR_BGR2GRAY)
        circles = detect_circles(gray, 25, 25, 10, 100)
        drawn_image = draw_circles(testV_image, circles)
        write_images(drawn_image, 'TestV_Hough', testV_file_name)

    testR_images = process_images('AIN433_F23_PA2_Dataset_v1/TestR')
    for testR_file_name, testR_image in testR_images.items():
        gray = cv2.cvtColor(testR_image, cv2.COLOR_BGR2GRAY)
        circles = detect_circles(gray, 25, 25, 10, 100)
        drawn_image = draw_circles(testR_image, circles)
        write_images(drawn_image, 'TestR_Hough', testR_file_name)


def make_image_512_512(image):
    h, w = image.shape
    if h != 512:
        image = cv2.resize(image, (int(512 * (w / h)), 512))
        h, w = image.shape
    crop = max((w - 512) // 2, 0)

    # Check if cropping is needed
    if w < 512:
        # Calculate the amount to add on both sides
        add_border = (512 - w) / 2
        if add_border == int(add_border):
            image = cv2.copyMakeBorder(image, 0, 0, int(add_border), int(add_border), cv2.BORDER_CONSTANT, value=0)
        else:
            image = cv2.copyMakeBorder(image, 0, 0, int(add_border), int(add_border) + 1, cv2.BORDER_CONSTANT, value=0)

    train_image_cropped = image[:, crop:crop + 512]
    return train_image_cropped


def part2():
    train_images = process_images("AIN433_F23_PA2_Dataset_v1/Train")
    X = []
    Y = []

    for train_file_name, train_image in train_images.items():
        gray = cv2.cvtColor(train_image, cv2.COLOR_BGR2GRAY)

        train_image_cropped = make_image_512_512(gray)
        hog_train = calculate_hog(train_image_cropped)

        file_name_splitted = train_file_name.split('_')
        label = f'{file_name_splitted[0]}_{file_name_splitted[1]}'
        X.append(hog_train)
        Y.append(label)

    combined_data = list(zip(X, Y))

    # Shuffle the combined data
    shuffled_data = shuffle(combined_data, random_state=42)  # You can set a specific random seed for reproducibility

    # Unpack the shuffled data back into X and Y
    X_shuffled, Y_shuffled = zip(*shuffled_data)

    # Convert back to numpy arrays if needed
    X_shuffled = np.array(X_shuffled)
    Y_shuffled = np.array(Y_shuffled)

    svm_model = LinearSVC(C=1.0, dual=False)
    svm_model.fit(X_shuffled, Y_shuffled)
    testV_images = process_images('AIN433_F23_PA2_Dataset_v1/TestV')
    for testV_file_name, testV_image in testV_images.items():
        printing_image = testV_image.copy()
        gray = cv2.cvtColor(testV_image, cv2.COLOR_BGR2GRAY)
        circles = detect_circles(gray, 25, 25, 10, 100)
        if circles is not None:
            y, x, r = circles
            circles = np.column_stack((x, y, r))

            for circle in circles:
                center = (circle[0], circle[1])
                radius = circle[2]
                y1 = center[0] - radius
                x1 = center[1] - radius
                y2 = center[0] + radius
                x2 = center[1] + radius
                circle_im = testV_image[x1:x2, y1:y2]
                gray_circle_im = cv2.cvtColor(circle_im, cv2.COLOR_BGR2GRAY)
                gray_circle_im = make_image_512_512(gray_circle_im)
                hog_test = calculate_hog(gray_circle_im)
                hog_test = hog_test.reshape(1, -1)
                predicted_label = svm_model.predict(hog_test)

                cv2.circle(printing_image, (center[0], center[1]), radius, (0, 255, 0),
                           2)  # Change color and thickness as needed
                cv2.putText(printing_image, predicted_label[0], (center[0] - 10, center[1] - 10),
                            cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)  # Adjust font and color as needed
        cv2.imwrite(f'/home/ubuntu/assignments/assignments/ain433/Assignment2/test_v/{testV_file_name}', printing_image)

    testR_images = process_images('AIN433_F23_PA2_Dataset_v1/TestR')
    for testR_file_name, testR_image in testR_images.items():
        printing_image = testR_image.copy()
        gray = cv2.cvtColor(testR_image, cv2.COLOR_BGR2GRAY)
        circles = detect_circles(gray, 25, 25, 10, 100)
        if circles is not None:
            y, x, r = circles
            circles = np.column_stack((x, y, r))

            for circle in circles:
                center = (circle[0], circle[1])
                radius = circle[2]
                y1 = center[0] - radius
                x1 = center[1] - radius
                y2 = center[0] + radius
                x2 = center[1] + radius
                circle_im = testR_image[x1:x2, y1:y2]
                gray_circle_im = cv2.cvtColor(circle_im, cv2.COLOR_BGR2GRAY)
                gray_circle_im = make_image_512_512(gray_circle_im)
                hog_test = calculate_hog(gray_circle_im)
                hog_test = hog_test.reshape(1, -1)
                predicted_label = svm_model.predict(hog_test)

                cv2.circle(printing_image, (center[0], center[1]), radius, (0, 255, 0),
                           2)  # Change color and thickness as needed
                cv2.putText(printing_image, predicted_label[0], (center[0] - 10, center[1] - 10),
                            cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)  # Adjust font and color as needed
        cv2.imwrite(f'/home/ubuntu/assignments/assignments/ain433/Assignment2/test_r/{testR_file_name}', printing_image)


if __name__ == "__main__":
    t1 = time.time()
    part1()
    t2 = time.time()
    part2()
    t3 = time.time()
    print(f'p1 = {t2 - t1}')
    print(f'p2 = {t3 - t2}')
    print(f'all = {t3 - t1}')
