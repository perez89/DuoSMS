package app.perez.support;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class RemoveProfileDialogFragment extends DialogFragment {
	
//	 Context mContext;
//
//	    public RemoveProfileDialogFragment() {
//	        mContext = getActivity();
//	    }
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        // Use the Builder class for convenient dialog construction
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setMessage("Delete this profile and all chat conversation with this contact?")
//               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                   public void onClick(DialogInterface dialog, int id) {
//                       // FIRE ZE MISSILES!
//                   }
//               })
//               .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                   public void onClick(DialogInterface dialog, int id) {
//                       // User cancelled the dialog
//                   }
//               });
//        // Create the AlertDialog object and return it
//        return builder.create();
//    }
	    public static RemoveProfileDialogFragment newInstance() {
	    	RemoveProfileDialogFragment frag = new RemoveProfileDialogFragment();
	        return frag;
	    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
  

        return new AlertDialog.Builder(getActivity())
        	.setMessage("Delete this profile and all chat conversation with this contact?")
                .setTitle("Remover")
                .setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                     
                        	if (getTargetFragment() != null) {
                        		System.out.println("getTargetFragment() != null");
    							getTargetFragment()
    									.onActivityResult(
    											3, -1
    											, null);
    						}
    						dialog.dismiss();
                        }
                    }
                )
                .setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	dialog.dismiss();
                        }
                    }
                )
                .create();
    }
    
}