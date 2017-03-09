# gatt-explorer
Android library for exploring and parsing the GATT-Profile of bluetooth low energy devices.

# data
To enter data go to the website htttp://akdeniza.com and use the formular to enter GATT data about BLE devices. The data will be saved on the following repo: github.com/akdeniza/gatt-explorer-database/

# usage

Add it in your root build.gradle at the end of repositories:
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 Add the dependency  
  
  dependencies {
	        compile 'com.github.akdeniza:gatt-explorer:1.0'
	}
    
 Create an an object of the GATTExplorer
    GATTExplorer gattExplorer = new GATTExplorer(content)
    
 Implement the two Interfaces ScanListener and GATTListener for data output.
    
 Use the methods startScan() and stopScan() to start and stop the Scan. Use the discoverGATT() method to discover the given device and parse the data using the given information via the formular on the website.

#License

    MIT License

    Copyright (c) 2017 akdeniza

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
