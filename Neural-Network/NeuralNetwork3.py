import numpy as np
import sys

learning_rate = 0.9
num_epochs = 60
num_hn = 20

has_CLA = len(sys.argv) > 1

training_images_file = str(sys.argv[1]) if has_CLA else "train_image.csv"
training_labels_file = str(sys.argv[2]) if has_CLA else "train_label.csv"
test_images_file = str(sys.argv[3]) if has_CLA else "test_image.csv"

training_images = np.genfromtxt(training_images_file, delimiter=',')
training_labels = np.genfromtxt(training_labels_file, delimiter=',')
test_images = np.genfromtxt(test_images_file, delimiter=',')


def scale_image(x):
    return x/2550.0


def sigmoid(x):
    return 1 / (1 + np.exp(-x))


def softmax(x):
    y = np.exp(x)
    s = np.sum(y)
    z = y/s
    return z


w01 = 0.1 * np.random.random_sample((784, num_hn))
w12 = 0.1 * np.random.random_sample((num_hn, 10))
b0 = 0.1 * np.random.random_sample((num_hn,))
b1 = 0.1 * np.random.random_sample((10,))

iw01 = np.copy(w01)
iw12 = np.copy(w12)


for _ in range(num_epochs):
    for training_index in range(len(training_images)):
        op0 = np.copy(training_images[training_index])
        op0 = scale_image(op0)
        ip1 = op0 @ w01 + b0
        op1 = sigmoid(ip1)
        ip2 = op1 @ w12 + b1
        op2 = softmax(ip2)
        pred_val = np.argmax(op2)
        pred_vec = np.zeros_like(op2)
        pred_vec[pred_val] = 1.0
        true_vec = np.zeros_like(op2)
        true_vec[int(training_labels[training_index])] = 1.0
        delta2 = op2 - true_vec
        update2 = np.outer(op1, delta2)
        R = w12 @ delta2
        m2 = 1 - op1
        M = op1 * m2
        H = M * R
        update1 = np.outer(op0, H)
        w01 -= learning_rate * update1
        w12 -= learning_rate * update2
        b1 -= learning_rate * delta2
        b0 -= learning_rate * H


f = open("test_predictions.csv", "w")

newline = ""
for test_index in range(len(test_images)):
    op0 = np.copy(test_images[test_index])
    op0 = scale_image(op0)
    ip1 = op0 @ w01 + b0

    op1 = sigmoid(ip1)

    ip2 = op1 @ w12 + b1

    op2 = softmax(ip2)

    pred_val = np.argmax(op2)

    f.write(f"{newline}{pred_val}")
    newline = "\n"

f.close()
