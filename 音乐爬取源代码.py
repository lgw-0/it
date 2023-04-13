import requests

headers = {
    'Cookie': '_ga=GA1.2.1574857442.1641026894; _gid=GA1.2.607461508.1641026894; kw_token=WRFKXNRRLBB',
    'csrf': 'WRFKXNRRLBB',
    'Host': 'www.kuwo.cn',
    'Referer': 'http://www.kuwo.cn/search/list',
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36'
}
key = input('请输入要下载的歌手：')
pn = input('需要下载第几页：')

url = 'https://www.kuwo.cn/api/www/search/searchMusicBykeyWord?key={}&pn={}'.format(key,pn)
resp = requests.get(url, headers=headers)

# print(resp.json())
for data in resp.json()['data']['list']:
    # print(data)
    rid = data['rid']
    # print(rid)
    name = data['name'].split('-')[0]
    # print(name)
    new_url = 'https://link.hhtjim.com/kw/{}.mp3'.format(rid)
    response = requests.get(new_url)
    with open('kuwo/%s.mp3' % name, 'wb') as f:
        f.write(response.content)
        print(name, '下载成功')
