import glob
import os
import cv2
import pandas as pd
import numpy as np
from sklearn.preprocessing import LabelEncoder

def preprocess_data(image_files, df):
    x = []
    y = []

    label_encoder = LabelEncoder()
    df['age'] = label_encoder.fit_transform(df['age'])

    for image_file in image_files:
        image = cv2.imread(image_file, cv2.IMREAD_GRAYSCALE) / 255.0
        x.append(image.flatten())
        y.append(df.loc[os.path.basename(image_file)]['age'])

    x = np.array(x)
    y = np.array(y)

    return x, y


class NN:
    def __init__(self, input_size=238700, hidden_size=10000, output_size=6):
        self.W1 = np.random.randn(hidden_size, input_size) * 0.01
        self.b1 = np.zeros((hidden_size, 1))
        self.W2 = np.random.randn(output_size, hidden_size) * 0.01
        self.b2 = np.zeros((output_size, 1))
        self.learning_rate = 0.01
        self.A1 = None
        self.A2 = None

    def forward(self, X):
        Z1 = np.dot(self.W1, X) + self.b1
        self.A1 = self.sigmoid(Z1)
        Z2 = np.dot(self.W2, self.A1) + self.b2
        self.A2 = self.sigmoid(Z2)

    def sigmoid(self, X):
        return 1 / (1 + np.exp(-X))

    def backward_propagation(self, X, Y):
        m = X.shape[1]

        dZ2 = self.A2 - Y
        dW2 = np.dot(dZ2, self.A1.T) / m
        db2 = np.sum(dZ2, axis=1, keepdims=True) / m

        dZ1 = np.dot(self.W2.T, dZ2) * self.A1 * (1 - self.A1)
        dW1 = np.dot(dZ1, X) / m
        db1 = np.sum(dZ1, axis=1, keepdims=True) / m
        self.update_parameters(dW1, db1, dW2, db2)

    def update_parameters(self, dW1, db1, dW2, db2):
        self.W1 -= self.learning_rate * dW1
        self.b1 -= self.learning_rate * db1
        self.W2 -= self.learning_rate * dW2
        self.b2 -= self.learning_rate * db2

    def compute_loss(self, Y, Y_pred):
        loss = np.sum(np.abs(Y-Y_pred)) / Y.shape[0]
        return loss

    def train(self, train_files, train_df, batch_size, num_epochs):
        for epoch in range(num_epochs):

            np.random.shuffle(train_files)

            for i in range(0, len(train_files), batch_size):
                train_batch_files = train_files[i:i + batch_size]
                train_X_batch, train_Y_batch = preprocess_data(train_batch_files, train_df)

                self.forward(train_X_batch.T)
                loss = self.compute_loss(train_Y_batch, self.A2)
                self.backward_propagation(train_X_batch, train_Y_batch)

                print(f"Epoch: {epoch + 1}, Batch: {i // batch_size + 1}, Loss: {loss}")

    def test(self, test_files, test_df, batch_size):

        np.random.shuffle(test_files)
        losses = []

        for i in range(0, len(test_files), batch_size):
            train_batch_files = train_files[i:i + batch_size]
            train_X_batch, train_Y_batch = preprocess_data(train_batch_files, test_df)

            self.forward(train_X_batch.T)
            loss = self.compute_loss(train_Y_batch, self.A2)
            losses.append(loss)

        return np.mean(losses)


if __name__ == '__main__':
    train_files = glob.glob('voice_dataset/train/*.png')
    test_files = glob.glob('voice_dataset/test/*.png')

    train_df = pd.read_csv('voice_dataset/train_data.csv')
    train_df = train_df.set_index('filename')

    test_df = pd.read_csv('voice_dataset/test_data.csv')
    test_df = test_df.set_index('filename')

    nn = NN()

    # Training and Validation
    nn.train(train_files, train_df, batch_size=1000, num_epochs=10)
    print(nn.test(test_files, test_df, batch_size=128))
