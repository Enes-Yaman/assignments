//
// Created by rotroq on 12/21/22.
//

#ifndef ASSIGNMENT4_PRIMARYNODE_H
#define ASSIGNMENT4_PRIMARYNODE_H


#include "AVLTree.h"
#include "LLRBTree.h"

class PrimaryNode {
public:
    string category;
    int height;
    PrimaryNode *left;
    PrimaryNode *right;
    // In this implementation I thought a node with two Tree attribution makes sense because
    // I wouldn't have to do 2 different implementations in main
    LLRBTree *llrbTree;
    AVLTree *avlTree;

    PrimaryNode(string category) {
        this->category = category;
        this->left = nullptr;
        this->right = nullptr;
        this->avlTree = new AVLTree();
        this->llrbTree = new LLRBTree();
        this->height = 1;
    }
};


#endif //ASSIGNMENT4_PRIMARYNODE_H
