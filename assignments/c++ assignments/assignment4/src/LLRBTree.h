//
// Created by rotroq on 12/20/22.
//

#ifndef ASSIGNMENT4_LLRBTREE_H
#define ASSIGNMENT4_LLRBTREE_H

using namespace std;

#include <queue>
#include "SecondaryNode.h"

class LLRBTree {
public:
    SecondaryNode *root;

    LLRBTree() {
        this->root = nullptr;
    }

    // Public function to insert a new node into the tree
    void insert(int price, string name) {
        this->root = insert(this->root, price, name);
        this->root->color = true; // Root is always black
    }

    // Public function to remove a node from the tree
    void remove(string name) {
        if (!isRed(this->root->left) && !isRed(this->root->right)) {
            this->root->color = false;
        }
        this->root = remove(this->root, name);
        if (this->root != nullptr) {
            this->root->color = true; // Root is always black
        }
    }

    // Public function to find a node
    SecondaryNode *Find(string &name) {
        return privateFind(root, name);
    }

    // Public function to print nodes on level order
    void printLevelOrder(SecondaryNode *root, string *output) {
        int h = height(root);
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
    SecondaryNode *privateFind(SecondaryNode *current, string &name) {
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

    // Private function to check if a node is red
    bool isRed(SecondaryNode *node) {
        if (node == nullptr) {
            return false; // NULL nodes are considered black
        }
        return !node->color;
    }

    // Private function to rotate left
    SecondaryNode *rotateLeft(SecondaryNode *node) {
        SecondaryNode *rightChild = node->right;
        node->right = rightChild->left;
        rightChild->left = node;
        rightChild->color = node->color;
        node->color = false; // Red
        return rightChild;
    }

    // Private function to rotate right
    SecondaryNode *rotateRight(SecondaryNode *node) {
        SecondaryNode *leftChild = node->left;
        node->left = leftChild->right;
        leftChild->right = node;
        leftChild->color = node->color;
        node->color = false; // Red
        return leftChild;
    }

    // Private function to flip the colors of a node and its children
    void flipColors(SecondaryNode *node) {
        node->color = !node->color;
        node->left->color = !node->left->color;
        node->right->color = !node->right->color;
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
        if (isRed(node->right) && !isRed(node->left)) {
            node = rotateLeft(node);
        }
        if (isRed(node->left) && isRed(node->left->left)) {
            node = rotateRight(node);
        }
        if (isRed(node->left) && isRed(node->right)) {
            flipColors(node);
        }
        return node;
    }

    // Private function to remove a node from the tree
    SecondaryNode *remove(SecondaryNode *node, string name) {
        if (node == nullptr) {
            return nullptr;
        }
        if (name < node->name) {
            if (!isRed(node->left) && !isRed(node->left->left)) {
                node = moveRedLeft(node);
            }
            node->left = remove(node->left, name);
        } else {
            if (isRed(node->left)) {
                node = rotateRight(node);
            }
            if (name == node->name && node->right == nullptr) {
                delete node;
                return nullptr;
            }
            if (!isRed(node->right) && !isRed(node->right->left)) {
                node = moveRedRight(node);
            }
            if (name == node->name) {
                SecondaryNode *temp = findMin(node->right);
                node->price = temp->price;
                node->name = temp->name;
                node->right = remove(node->right, temp->name);
            } else {
                node->right = remove(node->right, name);
            }
        }
        return balance(node);
    }

    // Private function to move a red node to the left
    SecondaryNode *moveRedLeft(SecondaryNode *node) {
        flipColors(node);
        if (isRed(node->right->left)) {
            node->right = rotateRight(node->right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    // Private function to move a red node to the right
    SecondaryNode *moveRedRight(SecondaryNode *node) {
        flipColors(node);
        if (isRed(node->left->left)) {
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    // Private function to balance the tree
    SecondaryNode *balance(SecondaryNode *node) {
        if (isRed(node->right)) {
            node = rotateLeft(node);
        }
        if (isRed(node->left) && isRed(node->left->left)) {
            node = rotateRight(node);
        }
        if (isRed(node->left) && isRed(node->right)) {
            flipColors(node);
        }
        return node;
    }

    // Private function to find the minimum value in the tree
    SecondaryNode *findMin(SecondaryNode *node) {
        if (node == nullptr || node->left == nullptr) {
            return node;
        }
        return findMin(node->left);
    }

    int height(SecondaryNode *node) {
        if (node == nullptr)
            return 0;
        else {
            int lheight = height(node->left);
            int rheight = height(node->right);
            if (lheight > rheight) {
                return (lheight + 1);
            } else {
                return (rheight + 1);
            }
        }
    }


};

#endif //ASSIGNMENT4_LLRBTREE_H
