String[] colors = {
  "white", "orange", "magenta", "light_blue",
  "yellow", "lime", "pink", "gray", "silver",
  "cyan", "purple", "blue", "brown", "green",
  "red", "black"
};

String[] oreKeys = {
  "dyeWhite", "dyeOrange", "dyeMagenta", "dyeLightBlue",
  "dyeYellow", "dyeLime", "dyePink", "dyeGray",
  "dyeLightGray", "dyeCyan", "dyePurple", "dyeBlue",
  "dyeBrown", "dyeGreen", "dyeRed", "dyeBlack"
};

String[] variants = {"classic", "modern", "pulsating", "lantern"};
String[] lampStyles = {"digital_lamp", "analog_lamp"};

String[] template;

void setup() {
  size(100,100);
  noLoop();
  
  int recipeCount = 0;
  for(int i=0; i < 16; i++) {
    String c = colors[i];
    String oreKey = oreKeys[i];
    
    for(String variant : variants) {
      for(String lampStyle : lampStyles) {
        recipeCount++;
        String fileName = c + "_" + variant + "_" + lampStyle
        String itemID = "dazzle:" + fileName;
        println("Generating recipe for " + itemID);
        
        template = loadStrings("_template" + lampStype + ".json");
        
        String[] outputFile = new String[template.length];
        int j = 0;
        for(String line : template) {
          String line2 = line.replace("ITEMID", itemID).replace("DYEID", oreKey);
          outputFile[j] = line2;
          j++;
        }
        saveStrings(fileName + ".json", outputFile);
        println("done");
      }
    }
  }
  
  println("Generated " + recipeCount + " recipes.");
}