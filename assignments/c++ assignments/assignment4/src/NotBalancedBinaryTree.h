//
// Created by rotroq on 12/21/22.
//

#ifndef ASSIGNMENT4_NOTBALANCEDBINARYTREE_H
#define ASSIGNMENT4_NOTBALANCEDBINARYTREE_H

#include "SecondaryNode.h"
#include <iostream>
#include <string>
#include <queue>

class NotBalancedBinaryTree {
public:
    NotBalancedBinaryTree(PrimaryNode *root) : root(root) {}

    ~NotBalancedBinaryTree() {
        delete root;
    }

    // Public function to insert a new node into the tree
    void Insert(PrimaryNode *node) {
        privateInsert(root, node);
    }

    // Public function to find a node
    PrimaryNode *Find(const std::string &value) const {
        return privateFind(root, value);
    }

    // Public function to get nodes on level order
    std::queue<PrimaryNode *> GetNodesInLevelOrder() {
        std::queue<PrimaryNode *> queue;
        std::queue<PrimaryNode *> queue2;
        if (!root) {
            return queue;
        }
        queue.push(root);
        queue2.push(root);
        while (!queue.empty()) {
            PrimaryNode *current = queue.front();
            queue.pop();
            if (current->left) {
                queue.push(current->left);
                queue2.push(current->left);
            }
            if (current->right) {
                queue.push(current->right);
                queue2.push(current->right);
            }
        }
        return queue2;
    }


private:
    // Private function to find the node with same name
    PrimaryNode *privateFind(PrimaryNode *current, const std::string &value) const {
        if (!current) {
            return nullptr;
        }
        if (current->category == value) {
            return current;
        }
        if (value < current->category) {
            return privateFind(current->left, value);
        } else {
            return privateFind(current->right, value);
        }
    }

    // Private function to insert a new node into the tree
    void privateInsert(PrimaryNode *&current, PrimaryNode *node) {
        if (!current) {
            current = node;
            return;
        }
        if (node->category < current->category) {
            privateInsert(current->left, node);
        } else {
            privateInsert(current->right, node);
        }
    }

    PrimaryNode *root;
};

#endif //ASSIGNMENT4_NOTBALANCEDBINARYTREE_H
