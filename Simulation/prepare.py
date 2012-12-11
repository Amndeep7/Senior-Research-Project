from sys import argv
from os import unlink

def main():
	if len(argv) < 3:
		raise Exception("Insufficient number of arguments")

	if argv[1] == "pre":
		for index in range(2, len(argv)):
			print("\npre", argv[index])
			
			# reads in the program
			f = open(argv[index], "r")
			program = f.read()
			f.close()
				
			# makes a copy of the program to recover from later
			f = open(argv[index] + ".copy", "w")
			f.write(program)
			f.close();

			# removes package statement
			program = program[program.index(";") + 3:]

			# writes out the modified program
			f = open(argv[index], "w")
			f.write(program)
			f.close()

	elif argv[1] == "post":
		for index in range(2, len(argv)):
			print("\npost", argv[index])

			# reads in the original program
			f = open(argv[index] + ".copy", "r")
			program = f.read()
			f.close()

			# copies over modified program
			f = open(argv[index], "w")
			f.write(program)
			f.close()

			# deletes copy of original program
			unlink(argv[index] + ".copy")

	else:
		raise Exception(argv[1] + " is not an option")

if __name__ == '__main__':
	main()
