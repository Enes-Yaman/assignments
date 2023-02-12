#include <iostream>
#include <vector>
#include <fstream>
#include "LLRBTree.h"
#include "AVLTree.h"
#include "PrimaryNode.h"
#include "NotBalancedBinaryTree.h"

using namespace std;
NotBalancedBinaryTree *balancedBinaryTree = new NotBalancedBinaryTree(nullptr);

vector<string> split(string &basicString, char splitter);  // basic splitter

void inputReader(string &inputFile); // input readers

void functionCaller(vector<string> lineData);


string outputAVL;
string outputLLRBT;

int main(int args, char **argv) {
    string input = argv[1];
    string output1 = argv[2];
    string output2 = argv[3];
    inputReader(input);

    // This two for deleting unwanted /n on end of the output
    outputAVL.pop_back();
    outputLLRBT.pop_back();

    ofstream outputWriter;
    outputWriter.open(output1);
    outputWriter << (outputAVL);
    outputWriter.close();
    outputWriter.open(output2);
    outputWriter << (outputLLRBT);
    outputWriter.close();
    return 0;
}

vector<string> split(string &basicString, char splitter) {
    string temp;
    vector<string> result;
    for (char i: basicString) {
        if (i != splitter) {
            temp.push_back(i);
        } else {
            result.push_back(temp);
            temp.clear();
        }
    }
    result.push_back(temp);
    return result;
}

// Reads the input data, refactor them with split function and call functionCaller to make them work
void inputReader(string &inputFile) {
    string keyString;
    ifstream keyStream;
    keyStream.open(inputFile);
    int counter = 0;
    if (keyStream.is_open()) {
        while (getline(keyStream, keyString)) {
            if (keyString.find('\n') != string::npos || keyString.find('\r') != string::npos) {
                keyString.pop_back();
            }
            vector<string> line = split(keyString, '\t');
            functionCaller(line);
        }
        keyStream.close();
    }
}

// Checks the command and does their works
void functionCaller(vector<string> lineData) {
    if (lineData[0] == "insert") {
        PrimaryNode *node = balancedBinaryTree->Find(lineData[1]);
        if (!node) {
            // If there is no node on the balanced binary tree, create one
            node = new PrimaryNode(lineData[1]);
            balancedBinaryTree->Insert(node);
        }
        // add a new node on both two tree
        node->llrbTree->insert(stoi(lineData[3]), lineData[2]);
        node->avlTree->insert(stoi(lineData[3]), lineData[2]);
    } else if (lineData[0] == "updateData") {
        PrimaryNode *node = balancedBinaryTree->Find(lineData[1]);
        if (!node) {
            // If there is no node on the balanced binary tree, create one
            node = new PrimaryNode(lineData[1]);
            balancedBinaryTree->Insert(node);
        }
        // Change the node's price on both two tree
        node->avlTree->Find(lineData[2])->price = stoi(lineData[3]);
        node->llrbTree->Find(lineData[2])->price = stoi(lineData[3]);
    } else if (lineData[0] == "remove") {
        // Find the category with Find function and remove the node in both 2 tree
        PrimaryNode *node = balancedBinaryTree->Find(lineData[1]);
        node->avlTree->remove(lineData[2]);
        node->llrbTree->remove(lineData[2]);
    } else if (lineData[0] == "find") {
        // Find the category with Find function and if there is a category find the Secondary Node to print its data
        // After this part code may seem complicated because of the output data formation
        // I made the find and print Item part to one tree because, Output does not change according to trees, and
        // We do not make any changes to the structure of the tree.
        PrimaryNode *node = balancedBinaryTree->Find(lineData[1]);
        outputAVL += "command:find\t" + lineData[1] + "\t" + lineData[2] + "\n{";
        outputLLRBT += "command:find\t" + lineData[1] + "\t" + lineData[2] + "\n{";
        if (node) {
            SecondaryNode *node2 = node->llrbTree->Find(lineData[2]);
            if (node->llrbTree->Find(lineData[2])) {
                outputAVL += "\n\"" + lineData[1] + "\":\n\t" + '"' + lineData[2] + "\":\"" + to_string(node2->price) +
                             "\"\n";
                outputLLRBT +=
                        "\n\"" + lineData[1] + "\":\n\t" + '"' + lineData[2] + "\":\"" + to_string(node2->price) +
                        "\"\n";
            }
        }
        outputLLRBT += "}\n";
        outputAVL += "}\n";
    } else if (lineData[0] == "printItem") {
        // same with find
        PrimaryNode *node = balancedBinaryTree->Find(lineData[1]);
        outputAVL += "command:printItem\t" + lineData[1] + "\t" + lineData[2] + "\n{";
        outputLLRBT += "command:printItem\t" + lineData[1] + "\t" + lineData[2] + "\n{";
        if (node) {
            SecondaryNode *node2 = node->llrbTree->Find(lineData[2]);
            if (node->llrbTree->Find(lineData[2])) {
                outputAVL += "\n\"" + lineData[1] + "\":\n\t" + "\"" + lineData[2] + "\":\"" + to_string(node2->price) +
                             "\"\n";
                outputLLRBT +=
                        "\n\"" + lineData[1] + "\":\n\t" + "\"" + lineData[2] + "\":\"" + to_string(node2->price) +
                        "\"\n";
            }
        }
        outputLLRBT += "}\n";
        outputAVL += "}\n";

    } else if (lineData[0] == "printAllItemsInCategory") {
        // Finds the category and calls Tree's print function
        outputAVL += "command:printAllItemsInCategory\t" + lineData[1] + "\n{\n\"" + lineData[1] + "\":";
        outputLLRBT += "command:printAllItemsInCategory\t" + lineData[1] + "\n{\n\"" + lineData[1] + "\":";
        PrimaryNode *node = balancedBinaryTree->Find(lineData[1]);
        node->avlTree->printLevelOrder(node->avlTree->root, &outputAVL);
        node->llrbTree->printLevelOrder(node->llrbTree->root, &outputLLRBT);
        outputAVL += "}\n";
        outputLLRBT += "}\n";

    } else if (lineData[0] == "printAllItems") {
        // Gets all the categories and calls Tree's print function
        outputAVL += "command:printAllItems\n{\n";
        outputLLRBT += "command:printAllItems\n{\n";
        queue<PrimaryNode *> primaryNodes = balancedBinaryTree->GetNodesInLevelOrder();
        while (!primaryNodes.empty()) {
            outputAVL += '"' + primaryNodes.front()->category + "\":";
            outputLLRBT += '"' + primaryNodes.front()->category + "\":";
            PrimaryNode *node = balancedBinaryTree->Find(primaryNodes.front()->category);
            node->avlTree->printLevelOrder(node->avlTree->root, &outputAVL);
            node->llrbTree->printLevelOrder(node->llrbTree->root, &outputLLRBT);
            primaryNodes.pop();
        }
        outputAVL += "}\n";
        outputLLRBT += "}\n";
    }

}
