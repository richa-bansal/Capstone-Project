package com.village.wannajoin.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.village.wannajoin.FavoriteEventLocationData.EventLocationContract;
import com.village.wannajoin.FavoriteEventLocationData.EventLocationQueryHandler;
import com.village.wannajoin.R;
import com.village.wannajoin.model.Event;
import com.village.wannajoin.util.Constants;
import com.village.wannajoin.util.Util;


public class EventDetailFragment extends Fragment {



    private String mEventId;
    private Event mEvent;
    private ValueEventListener valueEventListener;
    DatabaseReference eventRef;
    FirebaseUser firebaseUser ;
    private GoogleMap mMap;
    private LatLng mEventLocationLatLng;
    TextView locationTextView;
    Button buttonStartDate;
    Button buttonStartTime;
    Button buttonEndDate;
    Button buttonEndTime;
    EditText notesEditText;
    LinearLayout locationLL;
    LinearLayout notesLL;
    ImageButton buttonFavLocation;

    private OnFragmentInteractionListener mListener;

    public EventDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EventDetailFragment.
     */

    public static EventDetailFragment newInstance(Event event) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.EVENT, event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEvent = getArguments().getParcelable(Constants.EVENT);
            mEventId = mEvent.getEventId();
        }
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_detail2, container, false);
        locationTextView = (TextView) view.findViewById(R.id.event_location);
        buttonStartDate = (Button) view.findViewById(R.id.event_start_date);
        buttonStartTime = (Button) view.findViewById(R.id.event_start_time);
        buttonEndDate = (Button) view.findViewById(R.id.event_end_date);
        buttonEndTime = (Button) view.findViewById(R.id.event_end_time);
        notesEditText = (EditText) view.findViewById(R.id.event_notes) ;
        locationLL = (LinearLayout) view.findViewById(R.id.location_layout);
        notesLL = (LinearLayout) view.findViewById(R.id.notes_layout);
        buttonFavLocation = (ImageButton) view.findViewById(R.id.fav_button);
        buttonStartDate.setClickable(false);
        buttonEndDate.setClickable(false);
        buttonStartTime.setClickable(false);
        buttonEndTime.setClickable(false);
        notesEditText.setKeyListener(null);

        eventRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_EVENTS).child(mEventId);
        valueEventListener = eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mEvent = dataSnapshot.getValue(Event.class);
                    initializeUI();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }

    private void initializeUI(){
        if (mEvent.getLocation().equals("")) {
            setViewAndChildrenEnabled(locationLL, View.GONE);
        } else {
            setViewAndChildrenEnabled(locationLL, View.VISIBLE);
            locationTextView.setText(mEvent.getLocation());
            mEventLocationLatLng = new LatLng(mEvent.getLocationLat(), mEvent.getLocationLng());
            buttonFavLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addLocationToDataBase();
                }
            });
            //Add google map
            if (mEventLocationLatLng != null) {
                GoogleMapOptions options = new GoogleMapOptions();
                options.mapType(GoogleMap.MAP_TYPE_NORMAL)
                        .compassEnabled(false)
                        .zoomControlsEnabled(false);
                FragmentManager fmanager = getChildFragmentManager();

                SupportMapFragment supportmapfragment = SupportMapFragment.newInstance(options);
                FragmentTransaction fragmentTransaction = fmanager.beginTransaction();
                fragmentTransaction.add(R.id.location_map, supportmapfragment);
                fragmentTransaction.commit();
                supportmapfragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mMap = googleMap;

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mEventLocationLatLng, 10));
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(mEventLocationLatLng)
                                .title(mEvent.getTitle()));

                    }
                });

            }

        }
        buttonStartDate.setText(Util.getFormattedDateFromTimeStamp(mEvent.getFromTime()));
        buttonStartTime.setText(Util.getTimeFromTimeStamp(mEvent.getFromTime()));
        buttonEndDate.setText(Util.getFormattedDateFromTimeStamp(mEvent.getToTime()));
        buttonEndTime.setText(Util.getTimeFromTimeStamp(mEvent.getToTime()));
        if (mEvent.getNotes().equals("")) {
            setViewAndChildrenEnabled(notesLL, View.GONE);
        } else {
            setViewAndChildrenEnabled(notesLL, View.VISIBLE);
            notesEditText.setText(mEvent.getNotes());
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mEvent.getTitle());
        String status = mEvent.getEventMembers().get(firebaseUser.getUid());
        String[] statusArray = status.split("-");
        if (mListener != null) {
            mListener.onFragmentInteraction(statusArray[1], String.valueOf(mEvent.getFromTime()));
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String status, String fromTime);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventRef.removeEventListener(valueEventListener);
    }

    private static void setViewAndChildrenEnabled(View view, int type ) {
        view.setVisibility(type);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, type);
            }
        }
    }

    private void addLocationToDataBase(){
        ContentResolver contentResolver = getActivity().getContentResolver();
        EventLocationQueryHandler locationHandler = new EventLocationQueryHandler(contentResolver, getContext());
        ContentValues values = new ContentValues();
        values.put(EventLocationContract.LocationEntry.COLUMN_LOCATION_ID, mEvent.getLocationId());
        values.put(EventLocationContract.LocationEntry.COLUMN_LOCATION_ADDRESS, mEvent.getLocation());
        values.put(EventLocationContract.LocationEntry.COLUMN_LOCATION_LAT, String.valueOf(mEvent.getLocationLat()));
        values.put(EventLocationContract.LocationEntry.COLUMN_LOCATION_LNG, String.valueOf(mEvent.getLocationLng()));

        locationHandler.startInsert(0,null,EventLocationContract.LocationEntry.CONTENT_URI, values);

    }




}
