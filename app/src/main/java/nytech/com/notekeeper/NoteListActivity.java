package nytech.com.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class NoteListActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               startActivity(new Intent(NoteListActivity.this,NoteActivity.class));
            }
        });

        initializeDisplayContents();
    }

    private void initializeDisplayContents()
    {
        final ListView lvNotes = findViewById(R.id.lv_notes);
        List<NoteInfo> lsNoteInfo = DataManager.getInstance().getNotes();

        ArrayAdapter<NoteInfo> adapterNotes =
                new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,lsNoteInfo);
        lvNotes.setAdapter(adapterNotes);

        lvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent(NoteListActivity.this,NoteActivity.class);
                NoteInfo note = (NoteInfo) lvNotes.getItemAtPosition(i);
                intent.putExtra(NoteActivity.NOTE_INFO,note);
                startActivity(intent);
            }
        });
    }

}
