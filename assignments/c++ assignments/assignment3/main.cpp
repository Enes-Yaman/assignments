#include <iostream>
#include <vector>
#include "Orders.h"
#include "Queue.cpp"
#include "Worker.h"

using namespace std;
ofstream outputWriter;
PriorityQueue timeQueue;
PriorityOrderQueue orderQueue = *new PriorityOrderQueue(false);
Queue<Orders> cashierQueue;
PriorityOrderQueue outputQueue = *new PriorityOrderQueue(false);
int cashierCount;


void checkCashier(double time, Worker pWorker[]); //checks the cashiers with given time for if there is a cashier who
// isn't busy and can get a work from queue

void checkBarista(double time, Worker pWorker[]); //checks the baristas with given time for if there is a barista who
// isn't busy and can get a work from queue

void generalModel(double localTime, Worker cashiers[], Worker baristas[]); // same works in first model and second model
// like creating orders, timing and writing outputs

void firstModel(); // first model initialization; creating barista queues and give their references to the cashier
// and barista objects

void secondModel(); // second model initialization; same with first model, but it creates multiple queues and gives
// same references to 1 cashiers to 3 baristas.

vector<double> split(string &basicString, char splitter);  // basic splitter

void inputReader(string &inputFile); // input readers

vector<double> split(string &basicString, char splitter) {
    string temp;
    vector<double> result;
    for (char i: basicString) {
        if (i != splitter) {
            temp.push_back(i);
        } else {
            result.push_back(stod(temp));
            temp.clear();
        }
    }
    result.push_back(stod(temp));
    return result;
}

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
            vector<double> line = split(keyString, ' ');
            if (counter == 0) {
                cashierCount = int(*&line[0]);

            } else if (counter == 1) {
                counter++;
                continue;
            } else {
                orderQueue.enQueue(*new Orders(line[0], line[1], line[2], line[3]));
            }
            counter++;
        }
        keyStream.close();
    }
}

int main(int argv, char **args) {
    if (argv != 3) return 1;
    string input = args[1];
    string output = args[2];
    inputReader(input);
    outputWriter.open(output);
    firstModel();
    outputWriter << endl;
    secondModel();
    return 0;
}

void generalModel(double localTime, Worker cashiers[], Worker baristas[]) {
    PriorityOrderQueue orderQueueCopy = orderQueue;
    PriorityOrderQueue orderQueueCopy2 = orderQueue;
    while (!orderQueueCopy.isEmpty()) {
        timeQueue.enQueue(orderQueueCopy.deQueue().getArrivalTime());
    }
    while (!timeQueue.isEmpty()) {
        localTime = timeQueue.deQueue();
        if (!orderQueueCopy2.isEmpty() && orderQueueCopy2.back().getArrivalTime() == localTime) {
            cashierQueue.enQueue(orderQueueCopy2.back());
            orderQueueCopy2.deQueue();
        }
        checkCashier(localTime, cashiers);
        checkBarista(localTime, baristas);
    }
    outputWriter << localTime << endl;
    outputWriter << cashierQueue.returnMaxSize() << endl;
    if (baristas[0].BaristaQueue == baristas[1].BaristaQueue) {
        outputWriter << baristas[0].BaristaQueue->returnMaxSize() << endl;
    } else {
        for (int i = 0; i < cashierCount / 3; ++i) {
            outputWriter << baristas[i].BaristaQueue->returnMaxSize() << endl;
        }
    }

    for (int i = 0; i < cashierCount; ++i) {
        outputWriter << round(cashiers[i].busyTime / localTime * 100) / 100 << endl;
    }
    for (int i = 0; i < cashierCount / 3; ++i) {
        outputWriter << round(baristas[i].busyTime / localTime * 100) / 100 << endl;
    }
    while (!outputQueue.isEmpty()) {
        outputWriter << outputQueue.deQueue().getTurnAroundTime() << endl;
    }
}

void firstModel() {
    double localTime = 0;
    PriorityOrderQueue bq;
    Worker cashiers[cashierCount];
    Worker baristas[cashierCount / 3];
    for (int i = 0; i < cashierCount; ++i) {
        cashiers[i].BaristaQueue = &bq;
    }
    for (int i = 0; i < cashierCount / 3; ++i) {
        baristas[i].BaristaQueue = &bq;
    }
    generalModel(localTime, cashiers, baristas);
}

void secondModel() {
    double localTime = 0;
    PriorityOrderQueue baristaQueue[cashierCount / 3];
    Worker cashiers[cashierCount];
    Worker baristas[cashierCount / 3];
    for (int i = 0; i < cashierCount / 3; ++i) {
        cashiers[3 * i].BaristaQueue = &baristaQueue[i];
        cashiers[3 * i + 1].BaristaQueue = &baristaQueue[i];
        cashiers[3 * i + 2].BaristaQueue = &baristaQueue[i];
    }
    for (int i = 0; i < cashierCount / 3; ++i) {
        baristas[i].BaristaQueue = &baristaQueue[i];
    }
    generalModel(localTime, cashiers, baristas);
}

void checkBarista(double time, Worker pWorker[]) {
    for (int i = 0; i < cashierCount / 3; ++i) {
        if (pWorker[i].hasWork && pWorker[i].availableTime <= time) {
            pWorker[i].order.setTurnAroundTime(time - pWorker[i].order.getArrivalTime());
            outputQueue.enQueue(pWorker[i].order);
            if (!pWorker[i].BaristaQueue->isEmpty()) {
                Orders order = pWorker[i].BaristaQueue->deQueue();
                order.setTurnAroundTime(time);
                pWorker[i].order = order;
                pWorker[i].busyTime += order.getBrewTime();
                pWorker[i].availableTime = time + order.getBrewTime();
                timeQueue.enQueue(pWorker[i].availableTime);
            } else {
                pWorker[i].hasWork = false;
                pWorker[i].availableTime = 0;
            }
        } else if (!pWorker[i].hasWork && !pWorker[i].BaristaQueue->isEmpty()) {
            pWorker[i].hasWork = true;
            Orders order = pWorker[i].BaristaQueue->deQueue();
            order.setTurnAroundTime(time);
            pWorker[i].order = order;
            pWorker[i].busyTime += order.getBrewTime();
            pWorker[i].availableTime = time + order.getBrewTime();
            timeQueue.enQueue(pWorker[i].availableTime);
        }
    }
}

void checkCashier(double time, Worker pWorker[]) {
    for (int i = 0; i < cashierCount; ++i) {
        if (pWorker[i].hasWork && pWorker[i].availableTime <= time) {
            pWorker[i].BaristaQueue->enQueue(pWorker[i].order);
            if (!cashierQueue.isEmpty()) {
                Orders order = cashierQueue.deQueue();
                pWorker[i].availableTime = time + order.getOrderTime();
                pWorker[i].order = order;
                pWorker[i].busyTime += order.getOrderTime();
                timeQueue.enQueue(pWorker[i].availableTime);
            } else {
                pWorker[i].hasWork = false;
                pWorker[i].availableTime = 0;
            }

        } else if (!pWorker[i].hasWork && !cashierQueue.isEmpty()) {
            pWorker[i].hasWork = true;
            Orders order = cashierQueue.deQueue();
            pWorker[i].order = order;
            pWorker[i].availableTime = time + order.getOrderTime();
            pWorker[i].busyTime += order.getOrderTime();
            timeQueue.enQueue(pWorker[i].availableTime);
        }
    }
}