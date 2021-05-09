import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class homework {

    public static HashMap<String, ArrayList<String>> predicateSentenceMapping = new HashMap<>();
    public static int variableCounter = 0;

    public static void main(String[] args) throws IOException {
        File inputFile = new File("input.txt");
        FileWriter fileWriter = new FileWriter("output.txt");

        Scanner fileScanner = new Scanner(inputFile);
        
        ArrayList<String> inputFileLines = new ArrayList<>();
        while (fileScanner.hasNextLine()) {
            String data = fileScanner.nextLine();
            inputFileLines.add(data);
        }

        int numberOfQueries = Integer.parseInt(inputFileLines.get(0));
        ArrayList<String> queries = new ArrayList<>();
        for(int i=0; i<numberOfQueries; i++){
            queries.add(inputFileLines.get(i+1));
        }
        // System.out.println("> Queries - " + queries);

        int sizeOfProvidedKB = Integer.parseInt(inputFileLines.get(numberOfQueries + 1));
        ArrayList<String> providedKB = new ArrayList<>();
        for(int i=0; i<sizeOfProvidedKB; i++){
            providedKB.add(inputFileLines.get(i+numberOfQueries+2));
        }
        // System.out.println("> Provided KB - " + providedKB);

        ArrayList<String> modifiedKB = new ArrayList<>();
        HashMap<String,String> predicateVariableMapping = new HashMap<>();
        HashMap<Character,String> standardVariableMapping = new HashMap<>();
        int predicateCounter = 0;
        int tempvariableCounter = 0;
       

        for(int i=0; i<sizeOfProvidedKB; i++){
            String kbSentence = providedKB.get(i);
            String[] kbSentenceElements = kbSentence.split(" ");
            StringBuilder sb = new StringBuilder();
            for(int k=0; k<kbSentenceElements.length; k++){
                sb.append(kbSentenceElements[k]);
            }
            String modifiedKbSentence = sb.toString();
            sb.setLength(0);
            modifiedKbSentence = modifiedKbSentence.replace("=>", ">");

            // System.out.println("> Implication modified KB Sentence - " + modifiedKbSentence);

            String modifiedKbSentence2 = "";
            
            String modifiedKbSentence3 = "";

            modifiedKbSentence2 = modifiedKbSentence.replaceAll("[&>]","|");
            String[] modifiedSentencePredicateArray = modifiedKbSentence2.split("\\|");
        
            for(int k=0; k<modifiedSentencePredicateArray.length - 1; k++){
                if(modifiedSentencePredicateArray[k].contains("~")){
                    modifiedSentencePredicateArray[k] = modifiedSentencePredicateArray[k].replace("~","");
                }
                else{
                    modifiedSentencePredicateArray[k] = "~" + modifiedSentencePredicateArray[k];
                } 
            }

            for(int k=0; k<modifiedSentencePredicateArray.length; k++){
                modifiedKbSentence3 += modifiedSentencePredicateArray[k];
                if(k != modifiedSentencePredicateArray.length-1){
                    modifiedKbSentence3 += "|";
                }
            }

            // System.out.println("> Implication Removed and Negation moved inwards KB sentence(With Original Predicates) - " + modifiedKbSentence3);

            for(int k=0; k<modifiedKbSentence3.length(); k++){
                if(modifiedKbSentence3.charAt(k) == '(' || modifiedKbSentence3.charAt(k) == ','){
                    if(Character.isLowerCase(modifiedKbSentence3.charAt(k+1))){
                        
                        if(standardVariableMapping.containsKey(modifiedKbSentence3.charAt(k+1))){
                            String standardVariable = standardVariableMapping.get(modifiedKbSentence3.charAt(k+1));
                            modifiedKbSentence3 = modifiedKbSentence3.substring(0, k+1) + standardVariable + modifiedKbSentence3.substring(k+2);
                        }
                        else{
                            String standardVariable = "";
                            char nonStandardVariable = modifiedKbSentence3.charAt(k+1);
                            variableCounter++;
                            if(variableCounter > 0 && variableCounter <= 9){
                                standardVariable = "v000" + variableCounter;
                                modifiedKbSentence3 = modifiedKbSentence3.substring(0, k+1) + standardVariable + modifiedKbSentence3.substring(k+2);
                            }
                            else if(variableCounter > 9 && variableCounter <=99){
                                standardVariable = "v00" + variableCounter;
                                modifiedKbSentence3 = modifiedKbSentence3.substring(0, k+1) + standardVariable + modifiedKbSentence3.substring(k+2);
                            }
                            else if(variableCounter > 99 && variableCounter <=999){
                                standardVariable = "v0" + variableCounter;
                                modifiedKbSentence3 = modifiedKbSentence3.substring(0, k+1) + standardVariable + modifiedKbSentence3.substring(k+2);
                            }
                            else if(variableCounter > 999 && variableCounter <=9999){
                                standardVariable = "v" + variableCounter;
                                modifiedKbSentence3 = modifiedKbSentence3.substring(0, k+1) + standardVariable + modifiedKbSentence3.substring(k+2);
                            }
                            standardVariableMapping.put(nonStandardVariable, standardVariable);
                        }
                    }
                }
            }
            tempvariableCounter = variableCounter;
            standardVariableMapping.clear();

            // System.out.println("> Implication Removed and Negation moved inwards KB sentence(Original with standard variables) - " + modifiedKbSentence3);

            modifiedKB.add(modifiedKbSentence3);

            // System.out.println();
        }

        // System.out.println("> Modified KB - " + modifiedKB);
        // modifiedKB.add("~T(Kim)");
        for(int i=0; i<modifiedKB.size(); i++){
            String currentModifiedKbSentence = modifiedKB.get(i);
            // System.out.println("Current sentence - " + currentModifiedKbSentence);
            String[] currentModifiedKbSentenceArray = currentModifiedKbSentence.split("\\|");
            int flag = 0;
            for(int j=0; j<currentModifiedKbSentenceArray.length; j++){
                String currPredicate = currentModifiedKbSentenceArray[j];
                String pred = "";
                for(int k=0; k<currPredicate.length(); k++){
                    if(currPredicate.charAt(k) == '('){
                        break;
                    }
                    pred += currPredicate.charAt(k);
                }
                // System.out.println("Predicate Name - " + pred);
                if(predicateSentenceMapping.containsKey(pred)){
                    ArrayList<String> sentenceArrayList = predicateSentenceMapping.get(pred);
                    if(!sentenceArrayList.contains(currentModifiedKbSentence)){
                        sentenceArrayList.add(currentModifiedKbSentence);
                        predicateSentenceMapping.put(pred, sentenceArrayList);
                    }
                }
                else{
                    flag = 1;
                    ArrayList<String> sentenceArrayList = new ArrayList<>();
                    sentenceArrayList.add(currentModifiedKbSentence);
                    predicateSentenceMapping.put(pred, sentenceArrayList);
                }
                // System.out.println("Mapping - " + predicateSentenceMapping);
            }
        }

        // System.out.println("> Predicate Sentence Mapping - " + predicateSentenceMapping);

        String finalOutput = "";

        for(int i=0; i<queries.size(); i++){

            String querySentence = queries.get(i);
            if(querySentence.contains("~")){
                querySentence = querySentence.replace("~","");
            }
            else{
                querySentence = "~" + querySentence;
            }
            // System.out.println("Current sentence - " + currentModifiedKbSentence);
            // String[] currentModifiedKbSentenceArray = currentModifiedKbSentence.split("\\|");
            String pred = "";
            for(int k=0; k<querySentence.length(); k++){
                if(querySentence.charAt(k) == '('){
                    break;
                }
                pred += querySentence.charAt(k);
            }
            // System.out.println("Predicate Name - " + pred);
            if(predicateSentenceMapping.containsKey(pred)){
                ArrayList<String> sentenceArrayList = predicateSentenceMapping.get(pred);
                if(!sentenceArrayList.contains(querySentence)){
                    sentenceArrayList.add(querySentence);
                    predicateSentenceMapping.put(pred, sentenceArrayList);
                }
            }
            else{
                ArrayList<String> sentenceArrayList = new ArrayList<>();
                sentenceArrayList.add(querySentence);
                predicateSentenceMapping.put(pred, sentenceArrayList);
            }
            // System.out.println("Mapping - " + predicateSentenceMapping);

            variableCounter = tempvariableCounter;
            
            Stack<String> predicateToProveStack = new Stack<>();
            int counter = 0;
            String predicateToProve = queries.get(i);
            if(predicateToProve.contains("~")){
                predicateToProve = predicateToProve.replace("~","");
            }
            else{
                predicateToProve = "~" + predicateToProve;
            }

            predicateToProveStack.push(predicateToProve);

            boolean answer = performingResolutionAlgorithm(predicateToProveStack, counter);

            // System.out.println(">****** Final Resolution Answer ****** - " + answer);

            ArrayList<String> tempSentenceArrayList = predicateSentenceMapping.get(pred);
            tempSentenceArrayList.remove(querySentence);
            if(tempSentenceArrayList.isEmpty()){
                predicateSentenceMapping.remove(pred);
            }
            else{
                predicateSentenceMapping.put(pred, tempSentenceArrayList);
            }

            if(i != queries.size() - 1)
                finalOutput += Boolean.toString(answer).toUpperCase() + "\n";
            else
                finalOutput += Boolean.toString(answer).toUpperCase();
        }


        fileWriter.write(finalOutput);
        fileScanner.close();
        fileWriter.close();
    }

    private static boolean performingResolutionAlgorithm(Stack<String> predicateToProveStack, int counter) {
        // System.out.println("\n> Current predicate stack - " + predicateToProveStack);
        while(predicateToProveStack.empty() == false){
            String predicateToProve = "";
            predicateToProve = predicateToProveStack.pop();

            String predicateToProveNegation = "";
            // System.out.println("> Trying for predicate - " + predicateToProve);
            if(predicateToProve.contains("~")){
                predicateToProveNegation = predicateToProve.replace("~","");
            }
            else{
                predicateToProveNegation = "~" + predicateToProve;
            }
            // System.out.println("> Negation of above predicate - " + predicateToProveNegation);

            String predicateName = "";
            int flag = 0;
            for(int i=0; i<predicateToProveNegation.length(); i++){
                if(predicateToProveNegation.charAt(i) != '('){
                    predicateName += predicateToProveNegation.charAt(i);
                }
                else{
                    flag = i;
                    break;
                }
            }
            // System.out.println("> Predicate Name - " + predicateName);

            String[] predicateToProveNegationArguments = predicateToProveNegation.substring(flag+1, predicateToProveNegation.length()-1).split(",");
            // System.out.print("> Argument List 1 - ");
            // for(int i=0; i<predicateToProveNegationArguments.length; i++){
            //     System.out.print(predicateToProveNegationArguments[i] + ",");
            // }
            // System.out.println();
            
            if(predicateSentenceMapping.containsKey(predicateName)){
                ArrayList<String> sentencesWithPredicateName = predicateSentenceMapping.get(predicateName);
                // System.out.println("> Sentences with the predicate name - " + sentencesWithPredicateName);

                for(int i=0; i<sentencesWithPredicateName.size(); i++){
                    if(counter > 349){
                        // System.out.println("**Counter Exit!!**");
                        return false;
                    }
                    String[] predsArray = sentencesWithPredicateName.get(i).split("\\|");
                    ArrayList<String> predicatesFromSentence = new ArrayList<>();
                    for(int j=0; j<predsArray.length; j++){
                        predicatesFromSentence.add(predsArray[j]);
                    }
                    // System.out.println(">>> Current predicate stack - " + predicateToProveStack);
                    // System.out.println("> Predicates from sentence - " + predicatesFromSentence);
                    String matchedPredicate = "";
                    for(int j=0; j<predsArray.length; j++){
                        String currString = predsArray[j];
                        if(currString.contains(predicateName + "(")){
                            if(currString.contains("~") && predicateName.contains("~") || !currString.contains("~") && !predicateName.contains("~")){
                                matchedPredicate = predsArray[j];
                                break;
                            }
                        }
                    }
                    // System.out.println("> Matched Predicate - " + matchedPredicate);

                    flag = 0;
                    while(matchedPredicate.charAt(flag) != '('){
                        flag++;
                    }
                    String[] matchedPredicateArguments = matchedPredicate.substring(flag+1, matchedPredicate.length()-1).split(",");
                    System.out.print("> Argument List 2 - ");
                    for(int j=0; j<matchedPredicateArguments.length; j++){
                        System.out.print(matchedPredicateArguments[j] + ",");
                    }
                    System.out.println();
                    
                    HashMap<String,String> unificationCheckMapping = new HashMap<>();
                    boolean isUnificationPossible = false;

                    for(int j=0; j<matchedPredicateArguments.length; j++){
                        if(predicateToProveNegationArguments[j].equals(matchedPredicateArguments[j])){
                            isUnificationPossible = true;
                        }
                        else if(Character.isLowerCase(predicateToProveNegationArguments[j].charAt(0)) && Character.isLowerCase(matchedPredicateArguments[j].charAt(0))){
                            isUnificationPossible = true;
                        }
                        else if(Character.isLowerCase(predicateToProveNegationArguments[j].charAt(0)) && Character.isUpperCase(matchedPredicateArguments[j].charAt(0))){
                            if(unificationCheckMapping.containsKey(predicateToProveNegationArguments[j])){
                                String x = unificationCheckMapping.get(predicateToProveNegationArguments[j]);
                                if(!x.equals(matchedPredicateArguments[j])){
                                    isUnificationPossible = false;
                                    break;
                                }
                            }
                            unificationCheckMapping.put(predicateToProveNegationArguments[j], matchedPredicateArguments[j]);
                            isUnificationPossible = true;
                        }
                        else if(Character.isUpperCase(predicateToProveNegationArguments[j].charAt(0)) && Character.isLowerCase(matchedPredicateArguments[j].charAt(0))){
                            if(unificationCheckMapping.containsKey(matchedPredicateArguments[j])){
                                String x = unificationCheckMapping.get(matchedPredicateArguments[j]);
                                if(!x.equals(predicateToProveNegationArguments[j])){
                                    isUnificationPossible = false;
                                    break;
                                }
                            }
                            unificationCheckMapping.put(matchedPredicateArguments[j], predicateToProveNegationArguments[j]);
                            isUnificationPossible = true;
                        }
                        else{
                            isUnificationPossible = false;
                            break;
                        }
                    }
                    // System.out.println("> Is Unification Possible? - " + isUnificationPossible);

                
                    if(isUnificationPossible){
                        HashMap<String,String> unificationArgumentMapping = new HashMap<>();
                        for(int j=0; j<matchedPredicateArguments.length; j++){
                            if(Character.isLowerCase(predicateToProveNegationArguments[j].charAt(0)) && Character.isLowerCase(matchedPredicateArguments[j].charAt(0))){
                                if(unificationArgumentMapping.containsKey(matchedPredicateArguments[j])){
                                    unificationArgumentMapping.put(predicateToProveNegationArguments[j], unificationArgumentMapping.get(matchedPredicateArguments[j]));
                                }
                                else{
                                    unificationArgumentMapping.put(matchedPredicateArguments[j], predicateToProveNegationArguments[j]);
                                }
                                // unificationArgumentMapping.put(predicateToProveNegationArguments[j], matchedPredicateArguments[j]);
                            }
                            else if(Character.isLowerCase(predicateToProveNegationArguments[j].charAt(0))){
                                if(!unificationArgumentMapping.containsKey(predicateToProveNegationArguments[j])){
                                    unificationArgumentMapping.put(predicateToProveNegationArguments[j], matchedPredicateArguments[j]);
                                }
                                else if(unificationArgumentMapping.containsKey(predicateToProveNegationArguments[j]) && Character.isLowerCase(unificationArgumentMapping.get(predicateToProveNegationArguments[j]).charAt(0))){
                                    unificationArgumentMapping.put(unificationArgumentMapping.get(predicateToProveNegationArguments[j]), matchedPredicateArguments[j]);
                                    unificationArgumentMapping.put(predicateToProveNegationArguments[j], matchedPredicateArguments[j]);
                                }
                                else if(unificationArgumentMapping.containsKey(predicateToProveNegationArguments[j]) && Character.isLowerCase(matchedPredicateArguments[j].charAt(0))){
                                    unificationArgumentMapping.put(matchedPredicateArguments[j], unificationArgumentMapping.get(predicateToProveNegationArguments[j]));
                                }
                            }
                            else{
                                unificationArgumentMapping.put(matchedPredicateArguments[j], predicateToProveNegationArguments[j]);
                            }
                        }
                        // System.out.println("> Matched Arguments Mapping - " + unificationArgumentMapping);

                        ArrayList<String> stackArrayList = new ArrayList<>();
                        for(String str : predicateToProveStack){
                            // if(!stackArrayList.contains(str))
                                stackArrayList.add(str);
                        }
                        // System.out.println("> Stack ArrayList - " + stackArrayList);

                        for(int j=0; j<stackArrayList.size(); j++){
                            for(Map.Entry<String,String> entry : unificationArgumentMapping.entrySet()){
                                if(stackArrayList.get(j).contains(entry.getKey())){
                                    stackArrayList.set(j, stackArrayList.get(j).replace(entry.getKey(), entry.getValue()));
                                }
                            }
                        }
                        // System.out.println("> After Substitution of variables (StackArrayList) - " + stackArrayList);

                        for(int j=0; j<predicatesFromSentence.size(); j++){
                            for(Map.Entry<String,String> entry : unificationArgumentMapping.entrySet()){
                                if(predicatesFromSentence.get(j).contains(entry.getKey())){
                                    predicatesFromSentence.set(j, predicatesFromSentence.get(j).replace(entry.getKey(), entry.getValue()));
                                }
                            }
                        }
                        // System.out.println("> After Substitution of variables(PredicatesFromSentence) - " + predicatesFromSentence);
                        String tempPredicateToProveNegation = predicateToProveNegation;
                        for(Map.Entry<String,String> entry : unificationArgumentMapping.entrySet()){
                            if(tempPredicateToProveNegation.contains(entry.getKey())){
                                tempPredicateToProveNegation = tempPredicateToProveNegation.replace(entry.getKey(), entry.getValue());
                            }
                        }
                        // System.out.println("> After Substitution of variables(PredicateToProveNegation) - " + tempPredicateToProveNegation);

                        for(int j=0; j<predicatesFromSentence.size(); j++){
                            String unchangedPredicate = predicatesFromSentence.get(j);
                            // System.out.println("Predicate - " + unchangedPredicate);
                            if(!unchangedPredicate.equalsIgnoreCase(tempPredicateToProveNegation)){
                                // String negatedPredicate = "";
                                // if(unchangedPredicate.contains("~")){
                                //     negatedPredicate = unchangedPredicate.replace("~","");
                                // }
                                // else{
                                //     negatedPredicate = "~" + unchangedPredicate;
                                // }

                                // if(stackArrayList.contains(negatedPredicate)){
                                //     int index = stackArrayList.indexOf(negatedPredicate);
                                //     stackArrayList.remove(index);
                                // }
                                // else{
                                //     if(!stackArrayList.contains(unchangedPredicate))
                                //         stackArrayList.add(0,unchangedPredicate);
                                // }
                                if(!stackArrayList.contains(unchangedPredicate))
                                    stackArrayList.add(0,unchangedPredicate);
                            }
                        }
                        // System.out.println("> Stack ArrayList(Changed) - " + stackArrayList);

                        HashMap<String,String> standardVariableMapping2 = new HashMap<>();

                        for(int t=0; t<stackArrayList.size(); t++){
                            String currSentence = stackArrayList.get(t);
                            for(int k=0; k<currSentence.length(); k++){
                                if(currSentence.charAt(k) == '(' || currSentence.charAt(k) == ','){
                                    if(Character.isLowerCase(currSentence.charAt(k+1))){
                                        String nonStandardVariable = currSentence.substring(k+1,k+6);
                                        if(standardVariableMapping2.containsKey(nonStandardVariable)){
                                            String standardVariable = standardVariableMapping2.get(nonStandardVariable);
                                            // modifiedKbSentence3 = modifiedKbSentence3.substring(0, k+1) + standardVariable + modifiedKbSentence3.substring(k+2);
                                            currSentence = currSentence.replace(nonStandardVariable, standardVariable);
                                        }
                                        else{
                                            String standardVariable = "";
                                            
                                            // System.out.println("Nonstandard - " + nonStandardVariable);
                                            variableCounter++;
                                            if(variableCounter > 0 && variableCounter <= 9){
                                                standardVariable = "v000" + variableCounter;
                                                currSentence = currSentence.replace(nonStandardVariable, standardVariable);
                                            }
                                            else if(variableCounter > 9 && variableCounter <=99){
                                                standardVariable = "v00" + variableCounter;
                                                currSentence = currSentence.replace(nonStandardVariable, standardVariable);
                                            }
                                            else if(variableCounter > 99 && variableCounter <=999){
                                                standardVariable = "v0" + variableCounter;
                                                currSentence = currSentence.replace(nonStandardVariable, standardVariable);
                                            }
                                            else if(variableCounter > 999 && variableCounter <=9999){
                                                standardVariable = "v" + variableCounter;
                                                currSentence = currSentence.replace(nonStandardVariable, standardVariable);
                                            }
                                            // System.out.println("Nonstandard - " + nonStandardVariable);
                                            // System.out.println("standard - " + standardVariable);
                                            standardVariableMapping2.put(nonStandardVariable, standardVariable);
                                        }
                                    }
                                }
                            }
                            stackArrayList.set(t, currSentence);
                        }

                        Stack<String> newPredicateToProveStack = new Stack<>();

                        for(String str : stackArrayList){
                            newPredicateToProveStack.push(str);
                        }

                        boolean ans = performingResolutionAlgorithm(newPredicateToProveStack, counter+1);

                        if(ans == true){
                            return true;
                        }

                    }

                }

                return false;
            }
            else{
                return false;
            }
        }

        return true;
        
    }
}