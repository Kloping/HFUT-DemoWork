#include <iostream>
#include <queue>
#include <set>
#include <vector>
#include <algorithm>

using namespace std;

void fifoAlgorithm(int n, const vector<int> &pages)
{
    queue<int> pageQueue;
    set<int> physicalBlocks;
    vector<vector<int>> blockChanges;
    int missCount = 0;

    for (int page : pages)
    {
        if (physicalBlocks.find(page) == physicalBlocks.end())
        {
            missCount++;
            if (physicalBlocks.size() < n)
            {
                physicalBlocks.insert(page);
                pageQueue.push(page);
            }
            else
            {
                int removedPage = pageQueue.front();
                pageQueue.pop();
                physicalBlocks.erase(removedPage);
                physicalBlocks.insert(page);
                pageQueue.push(page);
            }
        }

        vector<int> currentBlocks(physicalBlocks.begin(), physicalBlocks.end());
        sort(currentBlocks.begin(), currentBlocks.end());
        while (currentBlocks.size() < n)
        {
            currentBlocks.push_back(-1);
        }
        blockChanges.push_back(currentBlocks);
    }

    cout << "物理块变化情况：" << endl;
    for (const auto &blocks : blockChanges)
    {
        for (int block : blocks)
        {
            if (block != -1)
            {
                cout << block << " ";
            }
            else
            {
                cout << "- ";
            }
        }
        cout << endl;
    }
    double missRate = static_cast<double>(missCount) / pages.size() * 100;
    cout << "缺页率：" << missRate << "%" << endl;
}

int main()
{
    int n;
    cout << "请输入被分配的物理块个数（3或4）：";
    cin >> n;
    vector<int> pages = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1};
    fifoAlgorithm(n, pages);
    return 0;
}