# Faerite
<p>
  <a href="https://github.com/r5ne/faerite/releases/latest">
  <img src="https://img.shields.io/github/v/release/r5ne/faerite?style=flat-square"
    alt="GitHub Actions workflow status"/></a>
  <a href="https://github.com/r5ne/faerite/actions">
    <img src="https://img.shields.io/github/actions/workflow/status/r5ne/faerite/release.yml?event=push&label=workflow&style=flat-square"
      alt="GitHub Actions workflow status"/></a>
</p>
An interactive, hand-drawn pixel art atlas.

## Installation
#### v0.0.5+
Faerite can be downloaded and ran straight from the releases tab. Simply extract the downloaded zip for the corresponding operating system.

#### v0.0.1 - v0.0.4:  
```bash
# Using Java 25+.
# Note that v0.0.1 - v0.0.3 are only compiled for Linux.
java -jar faerite-[version number].jar
```
  
## Compiling from source
Requirements:
- openjdk 25.0.4+
- Apache Maven 3.9.16+
- Faerite v0.0.5+
```bash
# From the project root:
mvn clean compile javafx:jlink
jpackage --type app-image --runtime-image target/image --name Faerite --module faerite/faerite.Main --dest out
```
## Licence
Faerite is licenced under the [MIT Licence](https://github.com/r5ne/faerite/blob/master/LICENCE).
## Special Thanks
[Дима Дегтярёв](https://github.com/DimasikGit) for testing on Windows.
