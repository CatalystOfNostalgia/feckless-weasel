import csv
import sys
import fileinput

def calcRTT(filename, source):
	with open(filename, 'rt') as filename:
		newData=csv.reader(filename)
		synACK = "[SYN, ACK]"
		syn= "[SYN]"
		for row in newData
			line = row[6]
			if syn in line
				if source == row[4]
					print row[4]
			

def findSource(filename):
	with open(filename, 'rt') as filename:
		newData=csv.reader(filename)
		for row in newData
			line = row[6];
			if line == "[SYN, ACK]"
				calcRTT(newData, line[3])



def readCSV(filename):
	with open(filename, 'rt') as filename:
		newFile = open("newfile.txt", "w")
		data=csv.reader(filename)
		for row in data:
			line = row[6];
			syn= "[SYN]"
			synAck = "[SYN, ACK]"
				if syn not in line or synAck not in line
					newFile.write(row, "w")


if __name__ == '__main__':
	readCSV("packet with sins and acks")
	findSource("newfile.txt")
	




 