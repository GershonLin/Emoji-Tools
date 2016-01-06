/*
 * Emoji Tools helps users and developers of Android, iOS, and OS X extract, modify, and repackage Emoji fonts.
 * Copyright (C) 2015 - 2016 Mitch Talmadge
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact Mitch Talmadge at mitcht@liveforcode.net
 */

package net.liveforcode.emojitools.operations.packaging;

import net.liveforcode.emojitools.gui.dialogs.OperationProgressDialog;
import net.liveforcode.emojitools.operations.Operation;
import net.liveforcode.emojitools.operations.OperationWorker;
import net.liveforcode.emojitools.operations.extraction.ExtractionOperation;
import net.liveforcode.emojitools.operations.packaging.packagingthreads.AndroidPackagingWorker;

import java.io.File;

public class PackagingOperation extends Operation {

    private final File packagingDirectory;

    public PackagingOperation(File packagingDirectory) {
        this.packagingDirectory = packagingDirectory;
    }

    @Override
    protected OperationWorker getWorker() {
        //Check for .ttx file
        File ttxFile = null;
        File[] files = packagingDirectory.listFiles();
        if (files == null)
            return null;

        for (File file : files) {
            if (file.getName().endsWith(".ttx")) {
                ttxFile = file;
                break;
            }
        }

        if (ttxFile == null) {
            return null;
        }

        ExtractionOperation.TTXType ttxType = null;
        for (ExtractionOperation.TTXType type : ExtractionOperation.TTXType.values()) {
            if (type.getFileName().equals(ttxFile.getName())) {
                ttxType = type;
                break;
            }
        }

        if (ttxType == null)
            return null;

        switch (ttxType) {
            case ANDROID:
                return new AndroidPackagingWorker(this, new OperationProgressDialog("Packaging to NotoColorEmoji.ttf..."), packagingDirectory);
            case IOS:
            case OSX:
                //showMessageDialog("iOS and OSX Emoji Fonts cannot be created yet. This feature is in development.");
                return null;
            default:
                //showMessageDialog("The selected Emoji directory is invalid or cannot be packaged.");
                return null;
        }
    }
}