#define MAX_ORDER_SIZE 200
#include "Orders.h"
#include "vector"
#include <bits/stdc++.h>
template <typename T>
class Queue {
protected:
    T myQueue[MAX_ORDER_SIZE]{};
    int rear;
    int front;
    int maxSize = 0;
public:
    Queue() {
        front = -1;
        rear = -1;
    }

    bool isFull() const {
        if (front == 0 && rear == MAX_ORDER_SIZE - 1) {
            return true;
        }
        return false;
    }

    bool isEmpty() const {
        if (front == -1) return true;
        else return false;
    }

    virtual bool enQueue(T value) {
        if (isFull()) {
            return false;
        } else {
            if (front == -1) front = 0;
            rear++;
            myQueue[rear] = value;
            if (rear-front+1 > maxSize){
                maxSize = rear-front+1;
            }
            return true;

        }
    }
    T deQueue() {

        T value = myQueue[front];
        if (front == rear) {
            front = -1;
            rear = -1;
        } else {
            front++;
        }
        return (value);

    }

    T back(){
        T value = myQueue[front];
        return value;
    }
    int returnMaxSize(){
        return maxSize;
    }


};
class PriorityOrderQueue : public Queue<Orders>{
    bool toPrice = true;
public:
    explicit PriorityOrderQueue(bool toPrice){
        this->toPrice = toPrice;
    }
    PriorityOrderQueue()= default;
    bool enQueue(Orders value) override {
        if (isFull()) {
            return false;
        } else {
            if (front == -1) front = 0;
            rear++;
            myQueue[rear] = value;
            sortQueue(toPrice);
            return true;
        }
    }
private:
    void sortQueue(bool toprice) {
        std::vector<Orders> orders;
        while (front!=-1){
            orders.push_back(this->deQueue());
        }
        if (toprice)
        sort(orders.begin(),orders.end(),[](Orders o1 , Orders o2) {
            return (o1.getPriceOfOrder()<o2.getPriceOfOrder());
        });
        else{
            sort(orders.begin(),orders.end(),[](Orders o1 , Orders o2) {
                return (o1.getArrivalTime()>o2.getArrivalTime());
            });
        }
        while (!orders.empty()){
            this->Queue::enQueue(orders.back());
            orders.pop_back();
        }

    }
};
class PriorityQueue : public Queue<double>{
public:
    bool enQueue(double value) override {
        if (isFull()) {
            return false;
        } else {
            if (front == -1) front = 0;
            rear++;
            myQueue[rear] = value;
            sortQueue();
            return true;
        }
    }
private:
    void sortQueue() {
        std::vector<double> orders;
        while (front!=-1){
            orders.push_back( this->deQueue());
        }
        sort(orders.begin(),orders.end(),[](double o1 , double o2) {return o1>o2;});
        while (!orders.empty()){
            this->Queue::enQueue(orders.back());
            orders.pop_back();
        }
    }
};
