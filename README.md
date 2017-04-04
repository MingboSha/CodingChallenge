# Insight Data Engineer Coding Challenge

#### The original problem can be found [here](https://github.com/InsightDataScience/fansite-analytics-challenge).

### Introduction of running:
To run the program, just execute the run.sh file by ```./run.sh```, if it shows "permission denied", execute this line before you run the .sh file.
~~~~
chmod a+x ./run.sh
~~~~

### Approach:

* For Feature 1 and Feature 2, the idea is simple, while looping the log file line by line, build a HashMap to store the <host, frequency> and <resource, bytes> information, after that, loop over the HashMap and maintain a minHeap with size 10, thus get the answer. One thing need to mention is in the requests, some of the failed requests have bytes value as "-", while I calculated the top 10 resources, I ignored these requests.
* For Feature 3, while looping over the log file, I keep a HashMap as <timeStamp, cumulateSum>, so that I can easily calculate the window sum by subtraction and also I keep an ArrayList with all timeStamps with request as an index for the HashMap. After I get this HashMap, since there not necessarily has request on each second, and in our requirement the window not necessarily start on a event, I move the start of the window from beginning to the end and every time I move it, I keep two pointer on the timeStamp ArrayList to keep track of all the timeStamps with requests in the window. And I keep a minHeap with size 10 to maintain the top 10 windows.
* For Feature 4, I keep a HashMap <host, [FailedRequestTimeStamps]>, the key is the host ip, the value is a ArrayList with all the recent failed request within 20 sec, and the size of this ArrayList is maintained smaller than 3. Whenever an IP needed to be blocked, it will be put into a HashMap <host, blockStartTime>, to record the start time of block.

### Additional Feature:
* I added an additional feature, Feature 5, which can produce a report of the percentage of requests for each weekdays. The result for the 400+MB sample input is:

  Sunday: 9%

  Monday: 15%

  Tuesday: 14%

  Wednesday: 16%

  Thursday: 19%

  Friday: 13%

  Saturday: 9%

  Currently the output is stored at path ```./log_output/weekdays.txt``` as default


* I also added an additional feature, Feature 6, which can produce a report of the percentage of requests for each hour of a day. The result for the 400+MB input is:

  00:00 - 01:00 3%

  01:00 - 02:00 2%

  02:00 - 03:00 2%

  03:00 - 04:00 1%

  04:00 - 05:00 1%

  05:00 - 06:00 1%

  06:00 - 07:00 1%

  07:00 - 08:00 2%

  08:00 - 09:00 4%

  09:00 - 10:00 5%

  10:00 - 11:00 5%

  11:00 - 12:00 6%

  12:00 - 13:00 6%

  13:00 - 14:00 6%

  14:00 - 15:00 6%

  15:00 - 16:00 6%

  16:00 - 17:00 6%

  17:00 - 18:00 5%

  18:00 - 19:00 4%

  19:00 - 20:00 3%

  20:00 - 21:00 3%

  21:00 - 22:00 3%

  22:00 - 23:00 3%

  23:00 - 00:00 3%

  Currently the output is stored at path ```./log_output/dayHours.txt``` as default


