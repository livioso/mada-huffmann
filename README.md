mada: huffmann tree
=================
---

### Example execution
###### When using the following input file `input.txt`

``` 
Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod
tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,
quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo
consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non
proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
```
###### The program output looks like this:
```
Characters with frequency (as Integers):
{68=1, 69=1, 10=5, 76=1, 85=1, 32=63, 97=29, 98=3, 99=16, 100=18, 
101=37, 102=3, 103=3,104=1, 105=43, 108=21, 44=4, 109=17, 110=24, 
46=4, 111=29, 112=11, 113=5, 114=22, 115=18, 116=32, 117=28, 118=3, 120=3}

Huffmann tree:
(101, 37, 0000) 
(105, 43, 0001) 
(32, 63, 0010) 
(68, 1, 00110) 
(69, 1, 00111) 
(76, 1, 01000) 
(85, 1, 01001) 
(104, 1, 01010) 
(98, 3, 01011) 
(102, 3, 01100) 
(103, 3, 01101) 
(118, 3, 01110) 
(120, 3, 01111) 
(44, 4, 10000) 
(46, 4, 10001) 
(10, 5, 10010) 
(113, 5, 10011) 
(112, 11, 10100) 
(99, 16, 10101) 
(109, 17, 10110) 
(100, 18, 10111) 
(115, 18, 11000) 
(108, 21, 11001) 
(114, 22, 11010) 
(110, 24, 11011) 
(117, 28, 11100) 
(97, 29, 11101) 
(111, 29, 11110) 
(116, 32, 11111) 

EncodingMap as bitstrings:
{10=10010, 32=0010, 44=10000, 46=10001, 68=00110, 69=00111, 76=01000, 85=01001, 97=11101, 98=01011, 99=10101, 100=10111, 101=0000, 102=01100, 103=01101, 104=01010, 105=0001, 108=11001, 109=10110, 110=11011, 111=11110, 112=10100, 113=10011, 114=11010, 115=11000, 116=11111, 117=11100, 118=01110, 120=01111}

Filesize analysis
Length of original text: 446
Length of compressed text: 261
Percental difference: 58.52%
```

###### The generated `dec_tab.txt` will be where the format is `character1:encoding1-...`
```
68:00110-69:00111-10:10010-76:01000-85:01001-32:0010-97:11101-98:01011-99:10101-100:10111-101:0000-102:01100-103:01101-104:01010-105:0001-44:10000-108:11001-109:10110-46:10001-110:11011-111:11110-112:10100-113:10011-114:11010-115:11000-116:11111-117:11100-118:11110-120:01111
```

###### The encoded file `output.dat` looks like this
```
G´!¦9b¿³íåÛàUöðøš.Üh8ŽÚA‘ür¿ˆ˜·¯/…©íî£q¿7òçË=_´+û>ÐºÝô»‘Ÿ;OÈ66.Ü¬;ƒcÛBSàà·ì\¹ÃU÷ãí—3žÚ¾,õ~Ðà¶8çË¹ðh@ò•}kzþ•}¼ùßÄÁÁwŸs@Wö}¢–‚šîììòŸïÀœc*ŽsËýŸh8LãG¿-ó9éSºÿ‰õ9¢Àïåê×¡^ü«Êûþü·íÊšðÜ7ø9¿ì«ÌÓ¥>	yŒJá€×7ò·³‘ùwcbcåž¯Úå£
```

###### The output file `output.txt` looks like the input file of course. 😎

