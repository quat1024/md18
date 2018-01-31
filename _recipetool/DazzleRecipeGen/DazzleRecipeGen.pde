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
    
    println("Generating digital lamps");
    for(String variant : variants) {
      recipeCount++;
      String fileName = c + "_" + variant + "_digital_lamp";
      String itemID = "dazzle:" + fileName;
      println("Generating recipe for " + itemID);
      
      template = loadStrings("_template_" + variant + ".json");
      
      String[] outputFile = new String[template.length];
      int j = 0;
      for(String line : template) {
        String line2 = line.replace("ITEMID", itemID).replace("DYEID", oreKey);
        outputFile[j] = line2;
        j++;
      }
      saveStrings("out/" + fileName + ".json", outputFile);
    }
  }
  
  println("Generating analog lamps");
  for(int i=0; i < 16; i++) {
    String c = colors[i];
    String oreKey = oreKeys[i];
    
    for(String variant : variants) {
        recipeCount++;
        String analogName = c + "_" + variant + "_analog_lamp";
        String itemID = "dazzle:" + analogName;
        String digitalName = c + "_" + variant + "_digital_lamp";
        String digitalID = "dazzle:" + digitalName;
        println("Generating recipe for " + itemID);
        
        template = loadStrings("_template_toAnalog.json");
        
        String[] outputFile = new String[template.length];
        int j = 0;
        for(String line : template) {
          String line2 = line.replace("DIGITALID", digitalID).replace("ANALOGID", itemID);
          outputFile[j] = line2;
          j++;
        }
        saveStrings("out/" + analogName + ".json", outputFile);
    }
  }
  
  println("Generated " + recipeCount + " recipes.");
}