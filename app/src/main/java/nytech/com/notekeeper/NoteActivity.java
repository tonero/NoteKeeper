package nytech.com.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
    private Spinner mSpCourses;
    private EditText mEtNoteTitle;
    private EditText mEtNoteText;
    private boolean mIsCancelling;
    private int mNotePosition;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSpCourses = findViewById(R.id.sp_courses);
        mEtNoteTitle = findViewById(R.id.et_note_title);
        mEtNoteText = findViewById(R.id.et_note_text);


        ArrayAdapter<CourseInfo> adapterCourses =
                new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,lsCourses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpCourses.setAdapter(adapterCourses);

        readDisplayState();
        if(!mIsNewNote)
            populateViews(mSpCourses, mEtNoteTitle, mEtNoteText);
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

        if(mIsNewNote)
        {
            createNewNote();

        }else
        {
            mNote = DataManager.getInstance().getNotes().get(position);
        }

    }

    private void createNewNote()
    {
        DataManager dm = DataManager.getInstance();
        mNotePosition = dm.createNewNote();
        mNote = dm.getNotes().get(mNotePosition);

    }


    private void saveEditedNote()
    {
        mNote.setCourse((CourseInfo)mSpCourses.getSelectedItem());
        mNote.setTitle(mEtNoteTitle.getText().toString());
        mNote.setText(mEtNoteText.getText().toString());
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if(mIsCancelling)
        {
             if(mIsNewNote || mNote == null)
             {
                 DataManager.getInstance().removeNote(mNotePosition);
             }
        }else
        {
            saveEditedNote();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
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
        if (id == R.id.action_send_email)
        {
            sendEmail();
            return true;
        }else if(id == R.id.action_cancel)
        {
            cancelNoteSave();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void cancelNoteSave()
    {
        mIsCancelling = true;
        finish();
    }

    /**
     * Use this if you want to send to a specific email
     *        String email ="contact@nytech.team";
     *        intent.setData(Uri.parse("mailto:"));
     *        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
     *        Intent intent = new Intent(Intent.ACTION_SENDTO);
     */
    private void sendEmail()
    {
       String noteTitle = mEtNoteTitle.getText().toString();
       CourseInfo courseTitle = (CourseInfo) mSpCourses.getSelectedItem();

       String emailBody = "Check out what I learned in the pluralsight course \""
               +courseTitle.getTitle()+"\"\n"
               +mEtNoteText.getText().toString();

       Intent intent = new Intent(Intent.ACTION_SEND);
       intent.setType("message/rfc2822");


       intent.putExtra(Intent.EXTRA_SUBJECT,noteTitle);
       intent.putExtra(Intent.EXTRA_TEXT,emailBody);
       startActivity(intent);

    }

}
