package midterm.jrs0058.comp3710.csse.eng.auburn.edu.jrs0058HAIKU;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jonathan on 3/3/2015.
 */
public class Build_Haiku_Fragment extends Fragment {
    DisplayListener mCallback;
    View v;
    Activity act;
    Button addWord;
    WordsAdapter w;
    ArrayList<Words> selectedWords = new ArrayList<Words>();
    Words selectedWord;
    int selectedWordsSize = 0;
    final int LINE_1_AND_3_MAX_SYL = 5;
    final int LINE_2_MAX_SYL = 7;
    int currentLine = 1;
    int[] lineSyl = {0,0,0,0};
    ArrayList<Words> adjectives, nouns, verbs, others;
    //interface for talking to main activity
    public interface DisplayListener {
        public void displayHaiku(String line1, String line2, String line3);
    }
    @Override
    public void onAttach(Activity a)
    {
        super.onAttach(a);
        act = a;
        try{
            mCallback = (DisplayListener) a;
        }
        catch (ClassCastException e){
            throw new ClassCastException(a.toString() + "must implement OnNumberPressedListener");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.build_haiku_fragment, container, false);
        if (savedInstanceState != null)
        {
            currentLine = savedInstanceState.getInt("currentLine");
            selectedWordsSize = savedInstanceState.getInt("selectedWordsSize");
            for (int i = 0; i <selectedWordsSize; i++)
            {
                Words temp = new Words();
                temp.setWord(savedInstanceState.getStringArray("bundleWordArray")[i]);
                temp.setSyllables(savedInstanceState.getIntArray("bundleSyllableArray")[i]);
                temp.setLine(savedInstanceState.getIntArray("bundleLineArray")[i]);
                selectedWords.add(temp);
            }
            lineSyl = savedInstanceState.getIntArray("lineSyl");
        }
        setupButtons();
        if (selectedWordsSize > 0) //when state needs to be/is being restored
        {
            restore_state();
        }
        else
        {
            currentLine = 1;
        }
        return v;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentLine", currentLine);
        outState.putInt("selectedWordsSize", selectedWordsSize);
        String[] bundleWordArray = new String[selectedWordsSize];
        int[] bundleSyllableArray = new int[selectedWordsSize];
        int[] bundleLineArray = new int[selectedWordsSize];
        for (int i = 0; i < selectedWordsSize; i++)
        {
            bundleWordArray[i] = selectedWords.get(i).getWord();
            bundleSyllableArray[i] = selectedWords.get(i).getSyllables();
            bundleLineArray[i] = selectedWords.get(i).getLine();
        }
        outState.putStringArray("bundleWordArray", bundleWordArray);
        outState.putIntArray("bundleSyllableArray", bundleSyllableArray);
        outState.putIntArray("bundleLineArray", bundleLineArray);
        outState.putIntArray("lineSyl", lineSyl);
    }

    /*********************************************************************************************************************************************************************
     ******************  Sets up all button listeners
     *********************************************************************************************************************************************************************/
    public void setupButtons()
    {
        final Button start_over = (Button) v.findViewById(R.id.start_over);
        final Button displayHaiku = (Button) v.findViewById(R.id.display);
        final RadioGroup wordGroup = (RadioGroup) v.findViewById(R.id.buttonsRadioGroup);
        final Spinner wordSpinner = (Spinner) v.findViewById(R.id.word_spinner);
        final TextView line_1 = (TextView) v.findViewById(R.id.first_line);
        final TextView line_2 = (TextView) v.findViewById(R.id.second_line);
        final TextView line_3 = (TextView) v.findViewById(R.id.third_line);
        buildWordCollections();
        final LinearLayout spinner_layout = (LinearLayout) v.findViewById(R.id.spinner_layout);
        displayHaiku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.displayHaiku(line_1.getText().toString(), line_2.getText().toString(), line_3.getText().toString());
            }
        });
        addWord = (Button) v.findViewById(R.id.addWord);
        final Button deleteLastWord = (Button) v.findViewById(R.id.delete_last_word);

        /*********************************************************************************************************************************************************************
         ******************  AddWord button listener
         *********************************************************************************************************************************************************************/
        addWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLastWord.setVisibility(View.VISIBLE);
                Words tempW = createNewWord(selectedWord.getWord(), selectedWord.getSyllables());
                tempW.setLine(currentLine);
                tempW.setWordNumber(selectedWordsSize);
                if ((lineSyl[1] == LINE_1_AND_3_MAX_SYL) && (currentLine == 1))
                {
                    currentLine = 2;
                }
                if ((lineSyl[2] == LINE_2_MAX_SYL) && (currentLine == 2))
                {
                    currentLine = 3;
                }
                if ((currentLine == 1) && (lineSyl[currentLine] < LINE_1_AND_3_MAX_SYL))
                {
                    line_1.append(" " + selectedWord.getWord());
                }
                if ((currentLine == 2) && (lineSyl[currentLine] < LINE_2_MAX_SYL))
                {
                    line_2.append(" " + selectedWord.getWord());
                }
                if ((currentLine == 3) && (lineSyl[currentLine] < LINE_1_AND_3_MAX_SYL))
                {
                    line_3.append(" " + selectedWord.getWord());
                }
                if ((currentLine == 3) && (lineSyl[currentLine] == LINE_1_AND_3_MAX_SYL))
                {
                    spinner_layout.setVisibility(View.INVISIBLE);
                }
                else {
                    lineSyl[currentLine] += tempW.getSyllables();
                    selectedWords.add(tempW);
                    selectedWordsSize++;
                }
                if ((lineSyl[1] == LINE_1_AND_3_MAX_SYL) && (currentLine == 1))
                {
                    currentLine = 2;
                }
                if ((lineSyl[2] == LINE_2_MAX_SYL) && (currentLine == 2))
                {
                    currentLine = 3;
                }
                if ((currentLine == 3) && (lineSyl[currentLine] == LINE_1_AND_3_MAX_SYL))
                {
                    spinner_layout.setVisibility(View.INVISIBLE);
                }
                else {
                    switch (wordGroup.getCheckedRadioButtonId()) {
                        case R.id.adjectives:
                            w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item, sortWords(0));
                            wordSpinner.setAdapter(w);
                            break;
                        case R.id.nouns:
                            w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item, sortWords(1));
                            wordSpinner.setAdapter(w);
                            break;
                        case R.id.verbs:
                            w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item, sortWords(2));
                            wordSpinner.setAdapter(w);
                            break;
                        case R.id.other:
                            w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item, sortWords(3));
                            wordSpinner.setAdapter(w);
                            break;
                    }
                }
            }
        });

        /*********************************************************************************************************************************************************************
         ******************  deleteLastWord button listener
         *********************************************************************************************************************************************************************/
        deleteLastWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_layout.setVisibility(View.VISIBLE);
                String temp = "";
                if (lineSyl[currentLine] == 0)
                {
                    currentLine--;
                }
                switch(selectedWords.get(selectedWordsSize - 1).getLine())
                {
                    case 1: temp = line_1.getText().toString();
                        break;
                    case 2: temp = line_2.getText().toString();
                        break;
                    case 3: temp = line_3.getText().toString();
                        break;
                }
                if (temp.length() > 0) {
                    temp = new StringBuilder(temp).replace(temp.lastIndexOf(" "), temp.length(), "").toString();
                }
                lineSyl[selectedWords.get(selectedWordsSize - 1).getLine()] -= selectedWords.get(selectedWordsSize - 1).getSyllables();
                if ((lineSyl[selectedWords.get(selectedWordsSize - 1).getLine()] == 0) && (currentLine > 1))
                {
                    lineSyl[currentLine] = 0;
                    currentLine--;
                }
                switch(selectedWords.get(selectedWordsSize - 1).getLine())
                {
                    case 1: line_1.setText(temp);
                        break;
                    case 2: line_2.setText(temp);
                        break;
                    case 3: line_3.setText(temp);
                        break;
                }
                selectedWords.remove(selectedWordsSize - 1);
                selectedWordsSize--;
                selectedWords.trimToSize();
                if (selectedWordsSize == 0)
                {
                    currentLine = 1;
                    deleteLastWord.setVisibility(View.INVISIBLE);
                }
                if ((lineSyl[1] == LINE_1_AND_3_MAX_SYL) && (currentLine == 1))
                {
                    currentLine = 2;
                }
                if ((lineSyl[2] == LINE_2_MAX_SYL) && (currentLine == 2))
                {
                    currentLine = 3;
                }
                switch(wordGroup.getCheckedRadioButtonId())
                {
                    case R.id.adjectives: w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item, sortWords(0));
                        wordSpinner.setAdapter(w);
                        break;
                    case R.id.nouns: w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item,  sortWords(1));
                        wordSpinner.setAdapter(w);
                        break;
                    case R.id.verbs: w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item, sortWords(2));
                        wordSpinner.setAdapter(w);
                        break;
                    case R.id.other: w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item, sortWords(3));
                        wordSpinner.setAdapter(w);
                        break;
                }
            }
        });

        /*********************************************************************************************************************************************************************
         ******************  WordGroup radiogroup listener
         *********************************************************************************************************************************************************************/
        wordGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                displayHaiku.setVisibility(View.VISIBLE);
                start_over.setVisibility(View.VISIBLE);
                if ((currentLine == 3) && (lineSyl[currentLine] == LINE_1_AND_3_MAX_SYL))
                {
                    spinner_layout.setVisibility(View.INVISIBLE);
                }
                else
                {
                    spinner_layout.setVisibility(View.VISIBLE);
                }
                switch(checkedId)
                {
                    case R.id.adjectives: w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item, sortWords(0));
                        wordSpinner.setAdapter(w);
                        break;
                    case R.id.nouns: w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item,  sortWords(1));
                        wordSpinner.setAdapter(w);
                        break;
                    case R.id.verbs: w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item, sortWords(2));
                        wordSpinner.setAdapter(w);
                        break;
                    case R.id.other: w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item, sortWords(3));
                        wordSpinner.setAdapter(w);
                        break;
                }
            }
        });

        /*********************************************************************************************************************************************************************
         ******************  Spinner listener
         *********************************************************************************************************************************************************************/
        wordSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedWord = new Words();
                selectedWord = w.getItem(position);
                addWord.setText("ADD\n\"" + selectedWord.getWord().toUpperCase() + "\"\nTO THE HAIKU");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedWord = new Words();
                selectedWord = w.getItem(0);
                addWord.setText("ADD\n\"" + selectedWord.getWord().toUpperCase() + "\"\nTO THE HAIKU");
            }
        });

        /*********************************************************************************************************************************************************************
         ******************  StartOver button listener
         *********************************************************************************************************************************************************************/
        start_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_over.setVisibility(View.INVISIBLE);
                displayHaiku.setVisibility(View.INVISIBLE);
                wordGroup.clearCheck();
                spinner_layout.setVisibility(View.INVISIBLE);
                deleteLastWord.setVisibility(View.INVISIBLE);
                currentLine = 1;
                lineSyl[1] = 0;
                lineSyl[2] = 0;
                lineSyl[3] = 0;
                selectedWordsSize = 0;
                selectedWords = new ArrayList<Words>();
                selectedWord = new Words();
                line_1.setText("");
                line_2.setText("");
                line_3.setText("");
                switch(wordGroup.getCheckedRadioButtonId())
                {
                    case R.id.adjectives: w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item, sortWords(0));
                        wordSpinner.setAdapter(w);
                        break;
                    case R.id.nouns: w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item,  sortWords(1));
                        wordSpinner.setAdapter(w);
                        break;
                    case R.id.verbs: w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item, sortWords(2));
                        wordSpinner.setAdapter(w);
                        break;
                    case R.id.other: w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item, sortWords(3));
                        wordSpinner.setAdapter(w);
                        break;
                }
                start_over.setVisibility(View.INVISIBLE);
                displayHaiku.setVisibility(View.INVISIBLE);
            }
        });
    }

    /*********************************************************************************************************************************************************************
     ******************  To restore state for when display is called and this is placed on backstack
     *********************************************************************************************************************************************************************/
    public void restore_state()
    {
        final TextView line_1 = (TextView) v.findViewById(R.id.first_line);
        final TextView line_2 = (TextView) v.findViewById(R.id.second_line);
        final TextView line_3 = (TextView) v.findViewById(R.id.third_line);
        final RadioGroup wordGroup = (RadioGroup) v.findViewById(R.id.buttonsRadioGroup);
        final Spinner wordSpinner = (Spinner) v.findViewById(R.id.word_spinner);
        final Button deleteLastWord = (Button) v.findViewById(R.id.delete_last_word);
        for (int i = 0; i < selectedWordsSize; i++)
        {
            switch(selectedWords.get(i).getLine()){
                case 1: line_1.append(" " + selectedWords.get(i).getWord());
                    break;
                case 2: line_2.append(" " + selectedWords.get(i).getWord());
                    break;
                case 3: line_3.append(" " + selectedWords.get(i).getWord());
            }
        }
        deleteLastWord.setVisibility(View.VISIBLE);
        if (lineSyl[3] == LINE_1_AND_3_MAX_SYL)
        {
            final LinearLayout spinner_layout = (LinearLayout) v.findViewById(R.id.spinner_layout);
            spinner_layout.setVisibility(View.INVISIBLE);
        }
        else {
            switch (wordGroup.getCheckedRadioButtonId()) {
                case R.id.adjectives:
                    w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item, sortWords(0));
                    wordSpinner.setAdapter(w);
                    break;
                case R.id.nouns:
                    w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item, sortWords(1));
                    wordSpinner.setAdapter(w);
                    break;
                case R.id.verbs:
                    w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item, sortWords(2));
                    wordSpinner.setAdapter(w);
                    break;
                case R.id.other:
                    w = new WordsAdapter(getActivity(), android.R.layout.simple_spinner_item, sortWords(3));
                    wordSpinner.setAdapter(w);
                    break;
            }
        }
    }

    /*********************************************************************************************************************************************************************
    ******************  Used to createNewWord when new word is added, fixes memory/pointer problems I was having
     *********************************************************************************************************************************************************************/
    public Words createNewWord(String word, int syl)
    {
        Words w = new Words();
        w.setWord(word);
        w.setSyllables(syl);
        return w;
    }

    /*********************************************************************************************************************************************************************
     ****************** Sorts Words by number of syllables left in lines, argument is which radio button is selected
     *********************************************************************************************************************************************************************/
    public ArrayList<Words> sortWords(int whichWords)
    {
        ArrayList<Words> newList = new ArrayList<Words>();
        int numberOfSyl = 0;
        if (currentLine == 2)
        {
            numberOfSyl = (LINE_2_MAX_SYL - lineSyl[currentLine]);
        }
        else
        {
            numberOfSyl = (LINE_1_AND_3_MAX_SYL - lineSyl[currentLine]);
        }
        if (numberOfSyl == 0)
        {
            return newList;
        }
        ArrayList<Words> arr = new ArrayList<Words>();
        switch(whichWords)
        {
            case 0: arr = adjectives;
                break;
            case 1: arr = nouns;
                break;
            case 2: arr = verbs;
                break;
            case 3: arr = others;
                break;
        }
        for (int i = 0; i < arr.size(); i++)
        {
            if (arr.get(i).getSyllables() <= numberOfSyl)
            {
                newList.add(arr.get(i));
            }
        }
        return newList;
    }

    /*********************************************************************************************************************************************************************
     ****************** Puts words from resource string arrays into Words item arraylists to make my life easier
     *********************************************************************************************************************************************************************/
    public void buildWordCollections()
    {
        adjectives = new ArrayList<Words>();
        nouns = new ArrayList<Words>();
        verbs = new ArrayList<Words>();
        others = new ArrayList<Words>();
        String[] adjectivesArr = getResources().getStringArray(R.array.adjectives);
        String[] nounsArr = getResources().getStringArray(R.array.nouns);
        String[] verbsArr = getResources().getStringArray(R.array.verbs);
        String[] othersArr = getResources().getStringArray(R.array.other);
        for (int i = 0; i < verbsArr.length; i++)
        {
            Words tempWord = new Words();
            String tempString = verbsArr[i];
            tempWord.setSyllables(Integer.parseInt(tempString.substring(0, 1)));
            tempWord.setWord(tempString.substring(1));
            verbs.add(tempWord);
            if (i < nounsArr.length)
            {
                tempWord = new Words();
                tempString = nounsArr[i];
                tempWord.setSyllables(Integer.parseInt(tempString.substring(0, 1)));
                tempWord.setWord(tempString.substring(1));
                nouns.add(tempWord);
            }
            if (i < adjectivesArr.length)
            {
                tempWord = new Words();
                tempString = adjectivesArr[i];
                tempWord.setSyllables(Integer.parseInt(tempString.substring(0, 1)));
                tempWord.setWord(tempString.substring(1));
                adjectives.add(tempWord);
            }
            if (i < othersArr.length)
            {
                tempWord = new Words();
                tempString = othersArr[i];
                tempWord.setSyllables(Integer.parseInt(tempString.substring(0, 1)));
                tempWord.setWord(tempString.substring(1));
                others.add(tempWord);
            }
        }
    }
}
