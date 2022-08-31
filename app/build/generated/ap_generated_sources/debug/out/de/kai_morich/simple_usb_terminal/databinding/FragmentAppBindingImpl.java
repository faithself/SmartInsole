package de.kai_morich.simple_usb_terminal.databinding;
import de.kai_morich.simple_usb_terminal.R;
import de.kai_morich.simple_usb_terminal.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentAppBindingImpl extends FragmentAppBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.textView4, 1);
        sViewsWithIds.put(R.id.textView3, 2);
        sViewsWithIds.put(R.id.textView2, 3);
        sViewsWithIds.put(R.id.textView1, 4);
        sViewsWithIds.put(R.id.textView, 5);
        sViewsWithIds.put(R.id.textView5, 6);
        sViewsWithIds.put(R.id.textView6, 7);
        sViewsWithIds.put(R.id.textView7, 8);
        sViewsWithIds.put(R.id.leftFootFloat, 9);
        sViewsWithIds.put(R.id.rightFootFloat, 10);
        sViewsWithIds.put(R.id.bothFeetFloat, 11);
        sViewsWithIds.put(R.id.totalStep, 12);
        sViewsWithIds.put(R.id.stepFreq, 13);
        sViewsWithIds.put(R.id.heelImpactL, 14);
        sViewsWithIds.put(R.id.heelImpactR, 15);
        sViewsWithIds.put(R.id.guideline2, 16);
        sViewsWithIds.put(R.id.guideline4, 17);
        sViewsWithIds.put(R.id.resetBiggest, 18);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentAppBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 19, sIncludes, sViewsWithIds));
    }
    private FragmentAppBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[11]
            , (androidx.constraintlayout.widget.Guideline) bindings[16]
            , (androidx.constraintlayout.widget.Guideline) bindings[17]
            , (android.widget.TextView) bindings[14]
            , (android.widget.TextView) bindings[15]
            , (android.widget.TextView) bindings[9]
            , (android.widget.Button) bindings[18]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[13]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[1]
            , (android.widget.TextView) bindings[6]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[12]
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