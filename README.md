# Planumator

Project for the OOP2 subject at ETF.

## Run

```
mvn compile exec:java -Dexec.mainClass=io.github.GrbavaCigla.App
```

## TODO
- Move `ModelList` initializations out of `Context`
- Check error dialogs displays
- Check if the modifications are reflected in the simulation after stopping
- Add `importAll`/`exportAll` somewhere
- Add File menu
- Check if the UI is isolated from the logic and models
- Check what happens when you delete an airport that has a flight referencing it
- Add constraints to models