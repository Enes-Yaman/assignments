import os
import random
import warnings

import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import tensorflow as tf
from keras.src.applications import VGG16
from sklearn.metrics import confusion_matrix
from sklearn.preprocessing import LabelEncoder
from tensorflow.keras import layers, models
from tensorflow.keras.preprocessing.image import img_to_array, load_img
from tensorflow.keras.utils import to_categorical
import keras

warnings.filterwarnings("ignore")


def create_cnn_model(input_shape, num_classes):
    model = models.Sequential()
    model.add(layers.Conv2D(32, (3, 3), activation='relu', input_shape=input_shape))
    model.add(layers.MaxPooling2D((2, 2)))
    model.add(layers.Conv2D(64, (3, 3), activation='relu'))
    model.add(layers.MaxPooling2D((2, 2)))
    model.add(layers.Conv2D(128, (3, 3), activation='relu'))
    model.add(layers.Flatten())
    model.add(layers.Dense(64, activation='relu'))
    model.add(layers.Dense(num_classes, activation='softmax'))
    return model


def train_model(train_data, train_labels, val_data, val_labels, test_data, test_labels):
    histories = {}
    for lr in [0.001, 0.0001, 0.00001]:
        for batch_size in [128, 256]:
            model = create_cnn_model((256, 256, 3), 15)
            model.compile(optimizer=tf.compat.v1.train.AdamOptimizer(learning_rate=lr), loss='categorical_crossentropy',
                          metrics=['accuracy'])
            history = model.fit(train_data, train_labels, epochs=30, batch_size=batch_size,
                                validation_data=(val_data, val_labels))
            histories[(lr, batch_size)] = history
            test_model(model, test_data, test_labels)
    return histories


def test_model(model, test_data, test_labels):
    test_loss, test_acc = model.evaluate(test_data, test_labels)
    print('Test Loss:', test_loss, ', Test Accuracy:', test_acc)


def create_dataset(path, img_size):
    random.seed(32)
    all_classes = os.listdir(path)
    filtered_classes = [cls for cls in all_classes if len(os.listdir(os.path.join(path, cls))) >= 300]
    selected_classes = random.sample(filtered_classes, 15)

    def load_images_from_class(class_name, target_size=(img_size, img_size)):
        images = os.listdir(os.path.join(path, class_name))
        selected_images = random.sample(images, 300)

        processed_images = []
        for img in selected_images:
            img_path = os.path.join(path, class_name, img)
            image = load_img(img_path, target_size=target_size)
            image = img_to_array(image)
            image /= 255.0
            processed_images.append(image)
        return processed_images

    train_data, val_data, test_data = [], [], []
    train_labels, val_labels, test_labels = [], [], []

    label_encoder = LabelEncoder()
    encoded_labels = label_encoder.fit_transform(selected_classes)

    for idx, cls in enumerate(selected_classes):
        images = load_images_from_class(cls)
        train_data.extend(images[:200])
        val_data.extend(images[200:250])
        test_data.extend(images[250:])

        train_labels.extend([encoded_labels[idx]] * 200)
        val_labels.extend([encoded_labels[idx]] * 50)
        test_labels.extend([encoded_labels[idx]] * 50)

    train_data = np.array(train_data)
    val_data = np.array(val_data)
    test_data = np.array(test_data)
    train_labels = to_categorical(np.array(train_labels), num_classes=len(selected_classes))
    val_labels = to_categorical(np.array(val_labels), num_classes=len(selected_classes))
    test_labels = to_categorical(np.array(test_labels), num_classes=len(selected_classes))

    return train_data, val_data, test_data, train_labels, val_labels, test_labels


def plot_training_history(histories):
    for (lr, batch_size), history in histories.items():
        plt.figure(figsize=(12, 4))

        # Plot training & validation accuracy values
        plt.subplot(1, 2, 1)
        plt.plot(history.history['accuracy'])
        plt.plot(history.history['val_accuracy'])
        plt.title(f'Model Accuracy (lr={lr}, batch_size={batch_size})')
        plt.ylabel('Accuracy')
        plt.xlabel('Epoch')
        plt.legend(['Train', 'Val'], loc='upper left')

        # Plot training & validation loss values
        plt.subplot(1, 2, 2)
        plt.plot(history.history['loss'])
        plt.plot(history.history['val_loss'])
        plt.title(f'Model Loss (lr={lr}, batch_size={batch_size})')
        plt.ylabel('Loss')
        plt.xlabel('Epoch')
        plt.legend(['Train', 'Val'], loc='upper left')

        plt.show()


def part1(train_x, train_y, val_x, val_y, test_x, test_y):
    histories = train_model(train_x, train_y, val_x, val_y, test_x, test_y)
    plot_training_history(histories)


def fine_tune_vgg16(train_data, train_labels, val_data, val_labels, test_data, test_labels,
                    trainable_conv_layers=False):
    # Load VGG-16 model pre-trained on ImageNet, without the top FC layers
    base_model = VGG16(weights='imagenet', include_top=False, input_shape=(256, 256, 3))

    # Freeze layers of the base model
    if not trainable_conv_layers:
        for layer in base_model.layers:
            layer.trainable = False
    else:
        # Unfreeze the last two convolutional layers
        for layer in base_model.layers[-2:]:
            layer.trainable = True

    # Add custom FC layers on top of VGG-16
    x = layers.Flatten()(base_model.output)
    x = layers.Dense(1024, activation='relu')(x)
    predictions = layers.Dense(15, activation='softmax')(x)  # 15 classes
    model = models.Model(inputs=base_model.input, outputs=predictions)

    # Compile the model
    model.compile(optimizer=tf.compat.v1.train.AdamOptimizer(learning_rate=0.0001), loss='categorical_crossentropy',
                  metrics=['accuracy'])

    # Train the model
    history = model.fit(train_data, train_labels, epochs=10, batch_size=256, validation_data=(val_data, val_labels))

    # Evaluate on the test set
    test_loss, test_accuracy = model.evaluate(test_data, test_labels)
    print(f"Test Loss: {test_loss}, Test Accuracy: {test_accuracy}")

    # Confusion Matrix
    predictions = model.predict(test_data)
    cm = confusion_matrix(test_labels.argmax(axis=1), predictions.argmax(axis=1))
    plt.figure(figsize=(10, 8))
    sns.heatmap(cm, annot=True, fmt='d')
    plt.title('Confusion Matrix')
    plt.ylabel('Actual Labels')
    plt.xlabel('Predicted Labels')
    plt.show()

    return history


def part2(train_x, train_y, val_x, val_y, test_x, test_y):
    print("Fine-tuning only FC layers:")
    history_fc_only = fine_tune_vgg16(train_x, train_y, val_x, val_y, test_x, test_y, False)
    print(history_fc_only)
    print("\nFine-tuning last two Conv layers and FC layers:")
    history_conv_fc = fine_tune_vgg16(train_x, train_y, val_x, val_y, test_x, test_y,
                                      True)  # Further analysis can be added here based on the histories
    print(history_conv_fc)


def parse_annotations(annotations_path, img_width, img_height):
    with open(annotations_path, 'r') as file:
        annotations = file.read().strip().split('\n')
    images = []
    boxes = []
    for annotation in annotations:
        parts = annotation.split(' ')
        filename = parts[0]
        annotation_data = parts[1].split(',')
        x_min = float(annotation_data[0]) / img_width
        y_min = float(annotation_data[1]) / img_height
        x_max = float(annotation_data[2]) / img_width
        y_max = float(annotation_data[3]) / img_height
        box = np.array([x_min, y_min, x_max, y_max])
        images.append(filename)
        boxes.append(box)
    return images, boxes


def load_image(image_path, size):
    image = tf.io.read_file(image_path)
    image = tf.image.decode_jpeg(image, channels=3)
    image = tf.image.resize(image, size)
    image = image / 255.0
    return image


def data_generator(image_filenames, boxes, batch_size, directory, width, height):
    num_samples = len(image_filenames)
    while True:  # Loop forever so the generator never terminates
        for offset in range(0, num_samples, batch_size):
            batch_samples = image_filenames[offset:offset + batch_size]
            images = []
            batch_boxes = boxes[offset:offset + batch_size]

            # Load images and preprocess them
            for filename in batch_samples:
                image_path = os.path.join(directory, filename)
                image = load_image(image_path , size=(width,height))
                images.append(image)

            X_train = np.array(images)
            y_boxes = np.array(batch_boxes)

            # Yield the current batch of images and boxes
            yield X_train, y_boxes


def part3():
    train_dir = 'C:/Users/enesyaman/Desktop/assignments/assignments/ain433/Assignment4/Raccoon.v2-raw.yolov4pytorch/train'
    valid_dir = 'C:/Users/enesyaman/Desktop/assignments/assignments/ain433/Assignment4/Raccoon.v2-raw.yolov4pytorch/valid'
    test_dir = 'C:/Users/enesyaman/Desktop/assignments/assignments/ain433/Assignment4/Raccoon.v2-raw.yolov4pytorch/test'

    img_width, img_height = 256,256
    # Example usage
    train_images, train_boxes = parse_annotations(os.path.join(train_dir, '_annotations.txt'), img_width, img_height)
    val_images, val_boxes = parse_annotations(os.path.join(valid_dir, '_annotations.txt'), img_width, img_height)
    test_images, test_boxes = parse_annotations(os.path.join(test_dir, '_annotations.txt'), img_width, img_height)

    # Create the generators
    batch_size = 32

    steps_per_epoch = len(train_images) // batch_size
    if len(train_images) % batch_size > 0:
        steps_per_epoch += 1

    validation_steps = len(val_images) // batch_size
    if len(val_images) % batch_size > 0:
        validation_steps += 1

    test_steps = len(test_images) // batch_size
    if len(test_images) % batch_size > 0:
        test_steps += 1

    train_generator = data_generator(train_images, train_boxes, batch_size, train_dir, img_width, img_height)
    val_generator = data_generator(val_images, val_boxes, batch_size, valid_dir, img_width, img_height)
    test_generator = data_generator(test_images, test_boxes, batch_size, test_dir, img_width, img_height)

    part3_model(train_generator, val_generator, test_generator, steps_per_epoch, validation_steps, test_steps, img_width, img_height)


def part3_model(train_generator, val_generator, test_generator, steps_per_epoch, validation_steps, test_steps, img_width, img_height):
    # Define the base model for feature extraction
    input_layer = layers.Input(shape=(img_width, img_height, 3))  # Adjust the input size based on your dataset
    conv_base = layers.Conv2D(32, (3, 3), activation='relu')(input_layer)
    flattened = layers.Flatten()(conv_base)

    # Classification head
    classification_head = layers.Dense(256, activation='relu')(flattened)
    # For a single class, use 1 neuron and 'sigmoid' activation
    classification_output = layers.Dense(1, activation='sigmoid', name='class_output')(classification_head)

    # Regression head for bounding box prediction
    regression_head = layers.Dense(256, activation='relu')(flattened)
    regression_output = layers.Dense(4, activation='linear', name='box_output')(
        regression_head)  # 4 coordinates for the bounding box

    # Complete model
    model = models.Model(inputs=input_layer, outputs=[classification_output, regression_output])

    # Compile the model
    model.compile(optimizer=tf.keras.optimizers.Adam(learning_rate=1e-4),
                  loss={'box_output': 'mean_squared_error'},
                  metrics={'box_output': 'mse'})

    # Fit the model
    history = model.fit(
        train_generator,
        validation_data=val_generator,
        epochs=25,
        steps_per_epoch=steps_per_epoch,
        validation_steps=validation_steps
    )

    evaluation = model.evaluate(test_generator, steps=test_steps)
    print(f'Test loss: {evaluation[0]}, Test mean squared error: {evaluation[1]}')


if __name__ == '__main__':
    train_x, val_x, test_x, train_y, val_y, test_y = create_dataset('C:/Users/enesyaman/Desktop/assignments'
                                                                    '/assignments/ain433/Assignment4/archive'
                                                                    '/indoorCVPR_09/Images', 256)
    # part1(train_x, train_y, val_x, val_y, test_x, test_y)
    # part2(train_x, train_y, val_x, val_y, test_x, test_y)
    part3()
