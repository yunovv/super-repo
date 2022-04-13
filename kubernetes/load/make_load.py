#!/usr/bin/python3.8

import requests
from random import randrange
import concurrent.futures
import urllib3
import time

urllib3.disable_warnings()


def send_test_request(url):
    try:
        my_request = requests.get(url, timeout= 15)
        return [my_request.text,my_request.elapsed.total_seconds()]
    except requests.exceptions.RequestException:
        return None


def animals_requests():
    host = 'animals.yunovv.local'
    animals = ["cat","dog","elephant","fly"]
    animal = animals[randrange(0,4)]
    url = 'http://{}/api/animals/{}'.format(host,animal)
    my_result = send_test_request(url)

    return my_result


if __name__ == '__main__':
    pool_size = 1000
    requests_time_total = 0
    failed_count = 0
    try:
        start_time = time.time()
        with concurrent.futures.ThreadPoolExecutor(max_workers=10) as executor:
            futures_list = {executor.submit(animals_requests) for i in range(pool_size)}
            for future in concurrent.futures.as_completed(futures_list):
                if future.result() != None:
                    print('{} - {}'.format(future.result()[0].strip('\n'),future.result()[1]))
                    requests_time_total += future.result()[1]
                else:
                    failed_count += 1
                    print('timeout - ')
        print('Total requests: {}, time spent: {}'.format(pool_size,round(time.time()-start_time,2)))
        print('Average responce time = {}'.format(round(requests_time_total/pool_size)))
        print('Timed out requests = {}'.format(failed_count))
    except KeyboardInterrupt:
        pass

        