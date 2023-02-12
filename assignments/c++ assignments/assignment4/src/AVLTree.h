//
// Created by rotroq on 12/20/22.
//

#ifndef ASSIGNMENT4_AVLTREE_H
#define ASSIGNMENT4_AVLTREE_H

#include <iostream>
#include "SecondaryNode.h"

using namespace std;

class AVLTree {
public:
    SecondaryNode *root;

    AVLTree() {
        this->root = nullptr;
    }

    // Public function to insert a new node into the tree
    void insert(int data, string name) {
        this->root = insert(this->root, data, name);
    }

    // Public function to remove a node from the tree
    void remove(string name) {
        this->root = remove(this->root, name);
    }

    // Public function to find a node
    SecondaryNode *Find(string &value) const {
        return privateFind(root, value);
    }

    // Public function to print nodes on level order
    void printLevelOrder(SecondaryNode *root, string *output) {
        int h = getHeight(root);
        int i;
        for (i = 1; i <= h; i++) {
            *output += "\n\t";
            printCurrentLevel(root, i, output);
            output->pop_back();
        }
        if (i == 1) {
            *output += "{}";
        }
        *output += "\n";

    }

private:
    // Private function to print all nodes on same level
    void printCurrentLevel(SecondaryNode *node, int level, string *output) {
        if (node == nullptr)
            return;
        if (level == 1)
            *output += "\"" + node->name + "\":\"" + to_string(node->price) + "\",";
        else if (level > 1) {
            printCurrentLevel(node->left, level - 1, output);
            printCurrentLevel(node->right, level - 1, output);
        }
    }

    // Private function to find the node with same name
    SecondaryNode *privateFind(SecondaryNode *current, string &name) const {
        if (!current) {
            return nullptr;
        }
        if (current->name == name) {
            return current;
        }
        if (name < current->name) {
            return privateFind(current->left, name);
        } else {
            return privateFind(current->right, name);
        }
    }

    // Private function to get the height of a node
    int getHeight(SecondaryNode *node) {
        if (node == nullptr) {
            return 0;
        }
        return node->height;
    }

    // Private function to get the balance factor of a node
    int getBalanceFactor(SecondaryNode *node) {
        if (node == nullptr) {
            return 0;
        }
        return getHeight(node->left) - getHeight(node->right);
    }

    // Private function to update the height of a node
    void updateHeight(SecondaryNode *node) {
        if (node == nullptr) {
            return;
        }
        node->height = 1 + max(getHeight(node->left), getHeight(node->right));
    }

    // Private function to perform a left rotation on a node
    SecondaryNode *rotateLeft(SecondaryNode *node) {
        SecondaryNode *rightChild = node->right;
        node->right = rightChild->left;
        rightChild->left = node;
        if (node == this->root) root = rightChild;
        updateHeight(node);
        updateHeight(rightChild);
        return rightChild;
    }

    // Private function to perform a right rotation on a node
    SecondaryNode *rotateRight(SecondaryNode *node) {
        SecondaryNode *leftChild = node->left;
        node->left = leftChild->right;
        leftChild->right = node;
        if (node == this->root) root = leftChild;
        updateHeight(node);
        updateHeight(leftChild);
        return leftChild;
    }

    // Private function to balance a node
    SecondaryNode *balanceNode(SecondaryNode *node) {
        int balanceFactor = getBalanceFactor(node);
        if (balanceFactor > 1) {
            // Left-heavy tree so its needed right rotation but if node's lefts balance factor smaller than 0 ( left
            // part is heavy but in the left part right part is heavy) so the right rotation will not fix the
            // balancing problem so check this
            if (getBalanceFactor(node->left) < 0) {
                // Left-Right rotation
                node->left = rotateLeft(node->left);
            }
            // Right rotation
            return rotateRight(node);
        } else if (balanceFactor < -1) {
            // Same but right part of if statement
            if (getBalanceFactor(node->right) > 0) {
                // Right-Left rotation
                node->right = rotateRight(node->right);
            }
            // Left rotation
            return rotateLeft(node);
        }
        return node;
    }

    // Private function to insert a new node into the tree
    SecondaryNode *insert(SecondaryNode *node, int price, string name) {
        if (node == nullptr) {
            return new SecondaryNode(name, price);
        }
        if (name < node->name) {
            node->left = insert(node->left, price, name);
        } else {
            node->right = insert(node->right, price, name);
        }
        updateHeight(node);
        return balanceNode(node);
    }

    // Private function to find the minimum value in the tree
    SecondaryNode *findMin(SecondaryNode *node) {
        if (node == nullptr || node->left == nullptr) {
            return node;
        }
        return findMin(node->left);
    }

    // Private function to remove a node from the tree
    SecondaryNode *remove(SecondaryNode *node, string name) {
        if (node == nullptr) {
            return nullptr;
        }
        if (name < node->name) {
            node->left = remove(node->left, name);
        } else if (name > node->name) {
            node->right = remove(node->right, name);
        } else {
            if (node->left == nullptr && node->right == nullptr) {
                // Leaf node so just delete the node
                delete node;
                return nullptr;
            } else if (node->left == nullptr) {
                // Only right child
                SecondaryNode *temp = node->right;
                delete node;
                return temp;
            } else if (node->right == nullptr) {
                // Only left child
                SecondaryNode *temp = node->left;
                delete node;
                return temp;
            } else {
                // Both children present so find the smallest node and change their values to deleting node so
                //  we mustn't do complex deleting process
                SecondaryNode *temp = findMin(node->right);
                node->price = temp->price;
                node->name = temp->name;
                node->right = remove(node->right, temp->name);
            }
        }
        updateHeight(node);
        return balanceNode(node);
    }
};

#endif //ASSIGNMENT4_AVLTREE_H
