# Planumator

Project for the OOP2 subject at ETF.

## Run

```
mvn compile exec:java -Dexec.mainClass=io.github.GrbavaCigla.App
```

## TODO
- ~~Move `ModelList` initializations out of `Context`~~
- ~~Check if the modifications are reflected in the simulation after stopping~~
- Check if the UI is isolated from the logic and models
- Add `importAll`/`exportAll` somewhere
- Add File menu
- Check what happens when you delete an airport that has a flight referencing it
- Check error dialogs displays
- Add constraints to models