/**
 * Copyright (C) 2014 OpenTravel Alliance (info@opentravel.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opentravel.schemacompiler.codegen.impl;

import org.opentravel.schemacompiler.codegen.CodeGenerationFilenameBuilder;
import org.opentravel.schemacompiler.model.AbstractLibrary;
import org.opentravel.schemacompiler.model.TLLibrary;
import org.opentravel.schemacompiler.model.TLModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the <code>CodeGenerationFilenameBuilder</code> interface that can create default filenames for the
 * XML schema files associated with <code>AbstractLibrary</code> instances.
 * 
 * @author S. Livezey
 */
public class LibraryFilenameBuilder<L extends AbstractLibrary> implements CodeGenerationFilenameBuilder<L> {

    private Map<L,String> baseFilenameMap;

    /**
     * @see org.opentravel.schemacompiler.codegen.CodeGenerationFilenameBuilder#buildFilename(java.lang.Object,
     *      java.lang.String)
     */
    @Override
    public String buildFilename(L item, String fileExtension) {
        synchronized (this) {
            if (baseFilenameMap == null) {
                TLModel model = (item == null) ? null : item.getOwningModel();

                baseFilenameMap = initBaseFilenames( model );
            }
        }
        String fileExt = (fileExtension.length() == 0) ? "" : ("." + fileExtension);
        String filename = baseFilenameMap.get( item );

        if (filename == null) {
            filename = new FilenameDetails( item ).getFilename();
        }
        if (!filename.toLowerCase().endsWith( fileExt )) {
            filename += fileExt;
        }
        return filename;
    }

    /**
     * Initializes the filenames that should be used for each library in the model.
     * 
     * @param model the model that contains all libraries to which names should be assigned
     * @return Map&lt;L,String&gt;
     */
    @SuppressWarnings("unchecked")
    private Map<L,String> initBaseFilenames(TLModel model) {
        Map<L,String> filenameMap = new HashMap<>();

        if (model != null) {
            Set<FilenameDetails> filenameDetails = new HashSet<>();

            // Build the initial list of filename details
            for (AbstractLibrary library : model.getAllLibraries()) {
                filenameDetails.add( new FilenameDetails( library ) );
            }

            // Check for conflicts and continue attempting to resolve until no
            // more conflicts exist, or no further options are available.
            checkAndResolveConflicts( filenameDetails );

            for (FilenameDetails fd : filenameDetails) {
                filenameMap.put( (L) fd.getLibrary(), fd.getFilename() );
            }
        }
        return filenameMap;
    }

    /**
     * Checks the given list for conflicting filenames and resolves the conflicts if any exist.
     * 
     * @param filenameDetails the list of filename details for which to resolve conflicts
     */
    private void checkAndResolveConflicts(Set<FilenameDetails> filenameDetails) {
        boolean conflictsExist;

        do {
            Map<String,List<FilenameDetails>> detailsByFilename = buildFilenameMap( filenameDetails );

            conflictsExist = false;

            for (List<FilenameDetails> detailsList : detailsByFilename.values()) {
                if (detailsList.size() > 1) {
                    boolean changesMade = false;

                    for (FilenameDetails fd : detailsList) {
                        if (!fd.getNsComponents().isEmpty()) {
                            fd.setLibraryFilename( fd.getLibrary().getName() + "_" + fd.getNsComponents().remove( 0 ) );
                            changesMade = true;
                        }
                    }

                    // If no more namespace options are available, allow the conflict to exist. In this
                    // situation, there are other errors in the model that should not have allowed us to
                    // get this far. Exiting at this point will prevent us from getting stuck in an
                    // infinite loop.
                    conflictsExist |= changesMade;
                }
            }

        } while (conflictsExist);
    }

    /**
     * Builds a map that associates each filename with the filename details from the set provided.
     * 
     * @param filenameDetails the set of filename details
     * @return Map&lt;String, List&lt;FilenameDetails&gt;&gt;
     */
    private Map<String,List<FilenameDetails>> buildFilenameMap(Set<FilenameDetails> filenameDetails) {
        Map<String,List<FilenameDetails>> detailsByFilename;
        detailsByFilename = new HashMap<>();

        for (FilenameDetails fd : filenameDetails) {
            detailsByFilename.computeIfAbsent( fd.getFilename(), fn -> detailsByFilename.put( fn, new ArrayList<>() ) );
            detailsByFilename.get( fd.getFilename() ).add( fd );
        }
        return detailsByFilename;
    }

    /**
     * Encapsulates the various details of a library's filename (used during initialization).
     */
    private static class FilenameDetails {

        private AbstractLibrary library;
        private List<String> nsComponents = new ArrayList<>();
        private String libraryFilename;
        private String versionSuffix;

        /**
         * Constructor that assigns the initial values for each component of the filename details.
         * 
         * @param library the library to which a filename will be assigned
         */
        public FilenameDetails(AbstractLibrary library) {
            String baseNS;

            this.setLibrary( library );
            this.setLibraryFilename( library.getName() );

            if (library instanceof TLLibrary) {
                TLLibrary tlLibrary = (TLLibrary) library;

                baseNS = tlLibrary.getBaseNamespace();
                this.setVersionSuffix( "_" + tlLibrary.getVersion().replaceAll( "\\.", "_" ) );

            } else {
                baseNS = library.getNamespace();
                this.setVersionSuffix( "" );
            }

            if (baseNS.endsWith( "/" )) {
                baseNS = baseNS.substring( 0, baseNS.length() - 1 );
            }
            this.setNsComponents( new ArrayList<>( Arrays.asList( baseNS.split( "/" ) ) ) );
            Collections.reverse( this.getNsComponents() );
        }

        /**
         * Returns the filename as currently specified by these details.
         * 
         * @return String
         */
        public String getFilename() {
            return getLibraryFilename() + getVersionSuffix();
        }

        /**
         * Returns the value of the 'library' field.
         *
         * @return AbstractLibrary
         */
        public AbstractLibrary getLibrary() {
            return library;
        }

        /**
         * Assigns the value of the 'library' field.
         *
         * @param library the field value to assign
         */
        public void setLibrary(AbstractLibrary library) {
            this.library = library;
        }

        /**
         * Returns the value of the 'nsComponents' field.
         *
         * @return List&lt;String&gt;
         */
        public List<String> getNsComponents() {
            return nsComponents;
        }

        /**
         * Assigns the value of the 'nsComponents' field.
         *
         * @param nsComponents the field value to assign
         */
        public void setNsComponents(List<String> nsComponents) {
            this.nsComponents = nsComponents;
        }

        /**
         * Returns the value of the 'libraryFilename' field.
         *
         * @return String
         */
        public String getLibraryFilename() {
            return libraryFilename;
        }

        /**
         * Assigns the value of the 'libraryFilename' field.
         *
         * @param libraryFilename the field value to assign
         */
        public void setLibraryFilename(String libraryFilename) {
            this.libraryFilename = libraryFilename;
        }

        /**
         * Returns the value of the 'versionSuffix' field.
         *
         * @return String
         */
        public String getVersionSuffix() {
            return versionSuffix;
        }

        /**
         * Assigns the value of the 'versionSuffix' field.
         *
         * @param versionSuffix the field value to assign
         */
        public void setVersionSuffix(String versionSuffix) {
            this.versionSuffix = versionSuffix;
        }

    }

}
