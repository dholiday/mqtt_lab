from multiprocessing import Pool
import requests

def post(message):
    r = requests.post('http://localhost:8899/mqtt_lab/mqtt_producer', data={'message': message})
    if r.status_code is not 200:
        print(r)

if __name__ == '__main__':
    p = Pool(10)
    while True:
        p.map(post, ['foo'])
