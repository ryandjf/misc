import json
import urllib.request
import xlwt

headers = {'Authorization': '64ac49342abf8ff13ac569513fad057e'}

baseUrl = "https://jigsaw.thoughtworks.net/api/people?home_office="

idList = []

offices = ["Beijing", "Xi'an", "Wuhan", "Chengdu", "Shanghai", "Shenzhen"]

print("Start fetching employees ids")

for office in offices:

    urlWithoutPageNumber = baseUrl + office + "&page="

    pageNumber = 1

    print(office)

    while True:
        print(pageNumber)

        url = urlWithoutPageNumber + str(pageNumber)

        req = urllib.request.Request(url, headers=headers)

        page = urllib.request.urlopen(req).read()

        page = page.decode('utf-8')

        result = json.loads(s=page)

        idList = idList + list(map(lambda obj: obj["employeeId"], result))

        if len(result) < 100:
            break

        pageNumber = pageNumber + 1

print("There are totally " + str(len(idList)) + " employees")

baseUrl = "https://jigsaw.thoughtworks.net/api/assignments.json?overlaps=01-07-2018,01-09-2018&page="
pageNumber = 1
assignments = []
while True:
    print("fetching assigments for page: " + str(pageNumber))

    url = baseUrl + str(pageNumber)

    req = urllib.request.Request(url, headers=headers)

    page = urllib.request.urlopen(req).read()

    page = page.decode('utf-8')

    result = json.loads(s=page)

    test = list(map(lambda x: (x["account"]["name"], x["project"]["name"], x["project"]["type"]),
                    filter(lambda x: idList.__contains__(x["consultant"]["employeeId"]), result)))

    assignments = assignments + test

    if len(result) < 100:
        break

    pageNumber = pageNumber + 1

result = list(set(assignments))
result.sort(key=lambda x: (x[0], x[1]))

book = xlwt.Workbook()
sheet = book.add_sheet('result')
row = 0
for each in result:
    col = 0
    for index in range(0,3):
        sheet.write(row,col,each[index])
        col+=1
    row+=1
book.save('result.xls')