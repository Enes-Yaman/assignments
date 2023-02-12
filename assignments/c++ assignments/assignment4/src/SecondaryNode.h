//
// Created by rotroq on 12/20/22.
//

#ifndef ASSIGNMENT4_SECONDARYNODE_H
#define ASSIGNMENT4_SECONDARYNODE_H

#include <string>


using namespace std;

class SecondaryNode {
public:
    string name;
    int price;
    int height;
    SecondaryNode *left;
    SecondaryNode *right;
    bool color; //false for red; true for black

    SecondaryNode(string name, int price) {
        this->price = price;
        this->height = 1;
        this->left = nullptr;
        this->right = nullptr;
        this->name = name;
        this->color = false;
    }
};


#endif //ASSIGNMENT4_SECONDARYNODE_H
