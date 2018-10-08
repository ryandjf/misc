import requests

headers = {'Authorization': '64ac49342abf8ff13ac569513fad057e'}

people_url = 'https://jigsaw.thoughtworks.net/api/people.json'

peoples = requests.get(people_url, headers=headers, params={'staffing_office':'Chengdu'}).json()

print (peoples)



payload = {'overlaps': '01-07-2018,01-08-2018', 'page': '0'}

assignments_url = 'https://jigsaw.thoughtworks.net/api/assignments.json'


page = 0
while(True):
	payload['page'] = page
	r = requests.get(assignments_url, headers=headers, params=payload)
	assignments = r.json()
	print (assignments)
	if(len(assignments) ==0):
		break;
	page+=1