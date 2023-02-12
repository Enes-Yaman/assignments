#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

vector<int> split(const string &stringToSplit, char splitter);

vector<int> dotProduct(vector<int> keyV, int **keyA, int **mapA, const vector<int> &mapSizeV, vector<int> locationV, char *outputName);

vector<int> estimation(int point, vector<int> keyVector, const vector<int> &mapSizeVector, vector<int> locationV);

void out_of_range_controller(int eX, int eY, vector<int> mapSizeV, vector<int> keySizeV) noexcept(false);

int **arrayInitializer(int **array, char *inputFile);

int **arrayCreator(vector<int> sizeV, char *txtName);

void array_deleter(int **a, vector<int> size);

void outputWriter(const string &output, char *outputName);

int main(int argc, char **argv) {
    remove(argv[5]); // delete output.txt if exist
    vector<int> mapVector = split(argv[1], 'x');
    vector<int> keyVector = {stoi(argv[2]), stoi(argv[2])};
    vector<int> locationV = {keyVector[0] / 2, keyVector[1] / 2};
    int **keyArray = arrayCreator(keyVector, argv[4]);
    int **mapArray = arrayCreator(mapVector, argv[3]);

    while (locationV != vector<int>{-1, -1}) { // a while loop for is game ended
        locationV = dotProduct(keyVector, keyArray, mapArray, mapVector, locationV, argv[5]);
    }

    // memory allocation
    array_deleter(keyArray, keyVector);
    array_deleter(mapArray, mapVector);

    return 1;

}

vector<int> dotProduct(vector<int> keyV, int **keyA, int **mapA, const vector<int> &mapSizeV, vector<int> locationV, char *outputName) { // makes 'dot Product'
    int point = 0;
    for (int i = -(keyV[0] / 2); i <= keyV[0] / 2; ++i) {
        for (int j = -(keyV[1] / 2); j <= keyV[1] / 2; ++j) {
            point += mapA[locationV[0] + i][locationV[1] + j] * keyA[keyV[0] / 2 + i][keyV[1] / 2 + j];
        }
    }
    outputWriter(to_string(locationV[0]) + "," + to_string(locationV[1]) + ":" + to_string(point), outputName);
    return estimation(point, keyV, mapSizeV, locationV);
}

vector<int> estimation(int point, vector<int> keyVector, const vector<int> &mapSizeVector, vector<int> location) { // makes and estimation for if it valid movement or not.
    // if its valid, changes the location and returns it to dotProduct() than main;
    // if its not, recursive itself and change direction
    switch (point % 5) {
        case 0:
            return vector<int>{-1, -1};
        case 1:
            try {
                out_of_range_controller(location[0] - keyVector[0], location[1], mapSizeVector, keyVector);
                location[0] -= keyVector[0];
            } catch (bad_exception &ArrayIndexOutOfBoundsException) {
                location = estimation(2, keyVector, mapSizeVector, location);
            }
            return location;
        case 2:
            try {
                out_of_range_controller(location[0] + keyVector[0], location[1], mapSizeVector, keyVector);
                location[0] += keyVector[0];
            } catch (bad_exception &ArrayIndexOutOfBoundsException) {
                location = estimation(1, keyVector, mapSizeVector, location);
            }
            return location;
        case 3:
            try {
                out_of_range_controller(location[0], location[1] + keyVector[1], mapSizeVector, keyVector);
                location[1] += keyVector[1];
            } catch (bad_exception &ArrayIndexOutOfBoundsException) {
                location = estimation(4, keyVector, mapSizeVector, location);
            }
            return location;
        case 4:
            try {
                out_of_range_controller(location[0], location[1] - keyVector[1], mapSizeVector, keyVector);
                location[1] -= keyVector[1];
            } catch (bad_exception &ArrayIndexOutOfBoundsException) {
                location = estimation(3, keyVector, mapSizeVector, location);
            }
            return location;
    }
    return vector<int>{-1, -1};
}

void out_of_range_controller(int eX, int eY, vector<int> mapSizeVector, vector<int> keySizeVector) noexcept(false) {
    // Checks if the movements valid or not, if its not throw an exception
    int q = keySizeVector[0] / 2;
    if (eX >= q && eY >= q && eX <= (mapSizeVector[0] - (q + 1)) && eY <= (mapSizeVector[1] - (q + 1))) {
        return;
    } else {
        throw bad_exception();
    }
}

vector<int> split(const string &stringToSplit, char splitter) {
    string temp;
    vector<int> result;
    for (char i: stringToSplit) {
        if (i != splitter) {
            temp.push_back(i);
        } else {
            result.push_back(stoi(temp));
            temp.clear();
        }
    }
    result.push_back(stoi(temp));
    return result;

}

int **arrayInitializer(int **array, char *inputFile) { // initialize the given array with given path
    string keyString;
    ifstream keyStream;
    keyStream.open(inputFile);
    int counter = 0;
    if (keyStream.is_open()) {
        while (getline(keyStream, keyString)) {
            vector<int> line = split(keyString, ' ');
            for (int i = 0; i < line.size(); ++i) {
                array[counter][i] = line[i];
            }
            counter++;
        }
        keyStream.close();
    }
    return array;
}

void array_deleter(int **a, vector<int> size) {
    for (int i = 0; i < size[0]; ++i) {
        a[i] = nullptr;
        delete[] a[i];
    }
}

int **arrayCreator(vector<int> sizeV, char *txtName) {
    int **temp;
    temp = new int *[sizeV[0]];
    for (int i = 0; i < sizeV[1]; ++i) {
        temp[i] = new int[sizeV[1]];
    }
    return arrayInitializer(temp, txtName);

}

void outputWriter(const string &output, char *outputName) {
    ofstream basicOfStream;
    basicOfStream.open(outputName, ios::app);
    basicOfStream << output << endl;
    basicOfStream.close();
}