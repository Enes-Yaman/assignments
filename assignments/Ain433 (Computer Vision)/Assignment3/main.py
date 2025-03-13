import cv2
import numpy as np


def extract_keypoints(image_path):
    image = cv2.imread(image_path)
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    sift = cv2.SIFT_create()
    key_points, descriptors = sift.detectAndCompute(gray, None)

    return key_points, descriptors


def match_keypoints(descriptors1, descriptors2):
    flann_index_params = dict(algorithm=1, trees=5)
    search_params = dict(checks=50)
    flann = cv2.FlannBasedMatcher(flann_index_params, search_params)
    matches = flann.knnMatch(descriptors1, descriptors2, k=2)
    good_matches = []
    for m, n in matches:
        if m.distance < 0.7 * n.distance:
            good_matches.append(m)

    return good_matches


def find_homography(keypoints1, keypoints2, good_matches):
    src_pts = np.float32([keypoints1[m.queryIdx].pt for m in good_matches]).reshape(-1, 1, 2)
    dst_pts = np.float32([keypoints2[m.trainIdx].pt for m in good_matches]).reshape(-1, 1, 2)

    # Use RANSAC to find the homography matrix
    homography, _ = cv2.findHomography(src_pts, dst_pts, cv2.RANSAC, 5.0)

    return homography


def merge_images(image1, image2, homography):
    result = cv2.warpPerspective(image1, homography, (image1.shape[1] + image2.shape[1], image1.shape[0]))
    result[0:image2.shape[0], 0:image2.shape[1]] = image2

    return result
