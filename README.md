# Weekend
Weekend is a convenient wrapper over [Greenmail](https://github.com/greenmail-mail-test/greenmail). It's functionality is read eml files from a folder and load them into an inbox provided by Greenmail. The inbox can then be accessed by your protocol of choice.

## Quick Start
1. Download the jar from the releases and run it from terminal.
2. By default it will load emails from the folder ***weekend*** present in your Downloads. i.e. if your username is ashish, then the default folder to read eml's is ***/home/ashish/Downloads/weekend***
3. A pop3 inbox can be accessed on your localhost now, on the port 3110. The login credentials to the inbox are admin/admin.
4. The lookup folder and login credentials can be tweaked by using the Advanced setup.

## Devleoper Setup
1. Install gradle, and ensure gradle is present in your path.
2. Run **gradle build eclipse** from the project root. This will download all the dependencies and generate the eclipse project. You can optionally do this by importing a gradle project in eclipse.

## Advanced Setup
To Be Documented

