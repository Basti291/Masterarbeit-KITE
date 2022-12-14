@echo off
rem Grid setup configuration for Windows 10
rem
rem Please check the Firefox and Chrome versions installed on your computer. If none installed, this script will download and install them.
rem
rem Please check and update the corresponding chromedriver and geckodriver versions from:
rem http://chromedriver.chromium.org/downloads
rem https://github.com/mozilla/geckodriver/releases
rem
rem Please check the selenium version from: https://selenium-release.storage.googleapis.com/

rem Set to TRUE to install Chrome and Firefox, or FALSE to skip this step if the correct versions are already installed.
set INSTALL_BROWSERS=FALSE

rem Set to TRUE to use localhost or FALSE to use the host's IP address
set LOCALHOST=TRUE

rem Selenium version
set SELENIUM_VERSION_SHORT=4.2
set SELENIUM_VERSION=4.2.1

rem Browser versions
set CHROME_VERSION=102
set FIREFOX_VERSION=101

rem ChromeDriver and GeckoDriver versions
set CHROMEDRIVER_VERSION=102.0.5005.61
set GECKO_VERSION=v0.31.0
