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
    def __init__(self, input_size=238700, hidden_size=50, output_size=6, hidden_layer_count=3):
        self.dZs = []
        self.W = [np.random.randn(input_size, hidden_size) * 0.01]
        self.dW = []
        self.B = [np.zeros((hidden_size, 1))]
        self.dB = []
        for i in range(1, hidden_layer_count - 1):
            self.W.append(np.random.randn(hidden_size, hidden_size) * 0.01)
            self.B.append(np.zeros((hidden_size, 1)))

        self.W.append(np.random.randn(hidden_size, output_size) * 0.01)
        self.B.append(np.zeros((output_size, 1)))
        self.learning_rate = 0.02
        self.Z = []

    def forward(self, X):
        self.Z = []
        input_ = X
        for i in range(len(self.W)):
            Z1 = np.dot(self.W[i].T, input_) + self.B[i]
            input_ = self.sigmoid(Z1)
            self.Z.append(input_)

    def sigmoid(self, X):
        return 1 / (1 + np.exp(-X))

    def backward_propagation(self, X, Y):
        m = X.shape[1]
        self.dZs = []
        self.dW = []
        self.dB = []
        dA = (self.Z[-1] - Y) / m

        for i in range(len(self.W) - 1, -1, -1):
            dZ = dA

            if i == 0:
                dW = np.dot(dZ, X)
            else:
                dW = np.dot(dZ, self.Z[i - 1].T)

            dB = np.sum(dZ, axis=1, keepdims=True)

            self.dZs.insert(0, dZ)
            self.dW.insert(0, dW)
            self.dB.insert(0, dB)

            dA = np.dot(self.W[i], dZ)

    def update_parameters(self):
        for i in range(len(self.W)):
            self.W[i] -= self.learning_rate * self.dW[i].T
            self.B[i] -= self.learning_rate * self.dB[i]

    def compute_loss(self, Y, Y_pred):
        m = len(Y)
        loss = -np.sum(Y * np.log(Y_pred) / m)
        return loss

    def train(self, train_files, train_df, batch_size, num_epochs):
        for epoch in range(num_epochs):
            losses = []
            np.random.shuffle(train_files)

            for i in range(0, len(train_files), batch_size):
                train_batch_files = train_files[i:i + batch_size]
                train_X_batch, train_Y_batch = preprocess_data(train_batch_files, train_df)

                self.forward(train_X_batch.T)
                loss = self.compute_loss(train_Y_batch, self.Z[-1])
                self.backward_propagation(train_X_batch, train_Y_batch)
                self.update_parameters()
                losses.append(loss)
            print(f"Epoch: {epoch + 1}, Loss: {np.mean(losses)}")

    def test(self, test_files, test_df, batch_size):

        np.random.shuffle(test_files)
        losses = []

        for i in range(0, len(test_files), batch_size):
            train_batch_files = train_files[i:i + batch_size]
            train_X_batch, train_Y_batch = preprocess_data(train_batch_files, test_df)

            self.forward(train_X_batch.T)
            loss = self.compute_loss(train_Y_batch, self.Z[-1])
            self.update_parameters()
            losses.append(loss)

        return np.mean(losses)

    def sigmoid_derivative(self, Z):
        return self.sigmoid(Z) * (1 - self.sigmoid(Z))


if __name__ == '__main__':
    train_files = glob.glob('voice_dataset/train/*.png')
    test_files = glob.glob('voice_dataset/test/*.png')

    train_df = pd.read_csv('voice_dataset/train_data.csv')
    train_df = train_df.set_index('filename')

    test_df = pd.read_csv('voice_dataset/test_data.csv')
    test_df = test_df.set_index('filename')

    nn = NN()

    # Training and Validation
    nn.train(train_files, train_df, batch_size=128, num_epochs=10)
    print(nn.test(test_files, test_df, batch_size=128))
