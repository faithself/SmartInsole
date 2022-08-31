package de.kai_morich.simple_usb_terminal.databinding;
import de.kai_morich.simple_usb_terminal.R;
import de.kai_morich.simple_usb_terminal.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentFeetFsrValueBindingImpl extends FragmentFeetFsrValueBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.feetBackground2, 1);
        sViewsWithIds.put(R.id.sensorR03, 2);
        sViewsWithIds.put(R.id.sensorR02, 3);
        sViewsWithIds.put(R.id.sensorR01, 4);
        sViewsWithIds.put(R.id.sensorR00, 5);
        sViewsWithIds.put(R.id.sensorL00, 6);
        sViewsWithIds.put(R.id.sensorL01, 7);
        sViewsWithIds.put(R.id.sensorL02, 8);
        sViewsWithIds.put(R.id.sensorL03, 9);
        sViewsWithIds.put(R.id.textViewR00, 10);
        sViewsWithIds.put(R.id.textViewR01, 11);
        sViewsWithIds.put(R.id.textViewR02, 12);
        sViewsWithIds.put(R.id.textViewR03, 13);
        sViewsWithIds.put(R.id.textViewL03, 14);
        sViewsWithIds.put(R.id.textViewL02, 15);
        sViewsWithIds.put(R.id.textViewL01, 16);
        sViewsWithIds.put(R.id.textViewL00, 17);
        sViewsWithIds.put(R.id.gCenter, 18);
        sViewsWithIds.put(R.id.bodyWeight, 19);
        sViewsWithIds.put(R.id.bottomNavigationView, 20);
        sViewsWithIds.put(R.id.fragmentContainerView, 21);
        sViewsWithIds.put(R.id.textView8, 22);
        sViewsWithIds.put(R.id.lowPowerIndL, 23);
        sViewsWithIds.put(R.id.lowPowerIndR, 24);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentFeetFsrValueBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 25, sIncludes, sViewsWithIds));
    }
    private FragmentFeetFsrValueBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[19]
            , (com.google.android.material.bottomnavigation.BottomNavigationView) bindings[20]
            , (android.widget.ImageView) bindings[1]
            , (androidx.fragment.app.FragmentContainerView) bindings[21]
            , (android.widget.ImageView) bindings[18]
            , (android.widget.TextView) bindings[23]
            , (android.widget.TextView) bindings[24]
            , (android.widget.ImageView) bindings[6]
            , (android.widget.ImageView) bindings[7]
            , (android.widget.ImageView) bindings[8]
            , (android.widget.ImageView) bindings[9]
            , (android.widget.ImageView) bindings[5]
            , (android.widget.ImageView) bindings[4]
            , (android.widget.ImageView) bindings[3]
            , (android.widget.ImageView) bindings[2]
            , (android.widget.TextView) bindings[22]
            , (android.widget.TextView) bindings[17]
            , (android.widget.TextView) bindings[16]
            , (android.widget.TextView) bindings[15]
            , (android.widget.TextView) bindings[14]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[11]
            , (android.widget.TextView) bindings[12]
            , (android.widget.TextView) bindings[13]
            );
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
            return variableSet;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}