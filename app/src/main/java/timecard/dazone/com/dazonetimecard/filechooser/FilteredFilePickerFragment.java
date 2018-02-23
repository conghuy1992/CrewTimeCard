package timecard.dazone.com.dazonetimecard.filechooser;

import android.os.Environment;
import android.support.annotation.NonNull;

import com.nononsenseapps.filepicker.FilePickerFragment;

import java.io.File;

/**
 * Extending a libraries classes to handle the wanted extension
 */
public class FilteredFilePickerFragment extends FilePickerFragment {

    // File extension to filter on
    private static final String[] EXTENSION = {".jpg", ".png", ".jpeg"};

    /**
     * @return The file extension. If file has no extension, it returns null.
     */
    private String getExtension(@NonNull File file) {
        String path = file.getPath();
        int i = path.lastIndexOf(".");
        if (i < 0) {
            return null;
        } else {
            return path.substring(i);
        }
    }

    @Override
    protected boolean isItemVisible(final File file) {
        if (!isDir(file) && (mode == MODE_FILE || mode == MODE_FILE_AND_DIR)) {
            for (String extension : EXTENSION) {
                if (extension.equalsIgnoreCase(getExtension(file))) {
                    return true;
                }
            }
            return false;
        }
        return isDir(file);
    }

    /**
     * prevents the filechooser from going into root directories.
     *
     * @see FilePickerFragment#goUp()
     */
    @Override
    public void goUp() {
        if (!mCurrentPath.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getPath())) {
            super.goUp();
        }
    }
}

