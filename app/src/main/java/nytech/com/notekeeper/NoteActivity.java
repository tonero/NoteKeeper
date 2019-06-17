package nytech.com.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity
{

    public static final String NOTE_POSITION = "nytech.com.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private List<CourseInfo> lsCourses = DataManager.getInstance().getCourses();
    private boolean mIsNewNote;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spCourses = findViewById(R.id.sp_courses);
        EditText etNoteTitle = findViewById(R.id.et_note_title);
        EditText etNoteText = findViewById(R.id.et_note_text);


        ArrayAdapter<CourseInfo> adapterCourses =
                new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,lsCourses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCourses.setAdapter(adapterCourses);

        readDisplayState();
        if(!mIsNewNote)
            populateViews(spCourses,etNoteTitle,etNoteText);
    }

    private void populateViews(Spinner spCourses, EditText etNoteTitle, EditText etNoteText)
    {
        int courseIndex = lsCourses.indexOf(mNote.getCourse());
        spCourses.setSelection(courseIndex);
        etNoteTitle.setText(mNote.getTitle());
        etNoteText.setText(mNote.getText());
    }

    private void readDisplayState()
    {
        Intent intent = getIntent();
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        mIsNewNote = position == POSITION_NOT_SET;

        if(!mIsNewNote)
            mNote = DataManager.getInstance().getNotes().get(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
