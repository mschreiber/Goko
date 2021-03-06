#!/bin/bash

#$1 is baseFolder, $2 is subfolders array
deleteFolders(){
	for file in $(curl -s -l -u $VAR1:$VAR2 $TARGET$1); 
	do
	 echo "Removing file www/download/$1$file"
	 curl -u $VAR1:$VAR2 $TARGET$1 -X "DELE $file"
	done
	echo "Removing folder www/download/$1"
	curl -u $VAR1:$VAR2 $TARGET -X "RMD $1"
	
}

# Clean the distant repository
cleanRepository(){
  echo "Cleaning repository..."
  deleteFolders update/$gokoVersion/binary/
  deleteFolders update/$gokoVersion/plugins/
  deleteFolders update/$gokoVersion/features/
}

# Clean the distant repository
exportRepository(){
  echo "Exporting repository..."
  # Switch to repository build location
  cd $TRAVIS_BUILD_DIR/org.goko.build.product/target/repository/
  # TODO change target to remove hardcoded 'nightly'
  find . -type f -exec curl --ftp-create-dirs -T {} -u $VAR1:$VAR2 $TARGET/$UPDATE_FOLDER/$gokoVersion/{} \;
}

# Let's do it
if [ $updateRepository == 'true' ]
then
  cleanRepository  
  exportRepository  
else
  echo "Skipped repository export..."
fi
