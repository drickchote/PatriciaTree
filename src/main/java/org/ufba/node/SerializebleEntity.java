package org.ufba.node;

import org.ufba.utils.ArrayUtils;

import java.io.Serializable;

import static org.ufba.Constants.POINTER_SIZE;

public abstract class SerializebleEntity{

    Integer diskOffset;

    String diskFile;

    String pointerToDisk; // type_offset

    public void setDiskOffset(int diskOffset) {
        this.diskOffset = diskOffset;
        if(diskFile != null && pointerToDisk == null){
            this.pointerToDisk = diskFile+"_"+diskOffset;
        }
    }

    public void setDiskFile(String diskFile) {
        this.diskFile = diskFile;
        if(diskOffset != null && pointerToDisk == null){
            this.pointerToDisk = diskFile+"_"+diskOffset;
        }
    }

    public String getPointerToDisk() {
        return pointerToDisk;
    }

    public char[] getPointerToDiskCharacters(){
        char [] characters = new char[POINTER_SIZE];

        if(pointerToDisk != null)
            ArrayUtils.copyStringToCharacter(this.pointerToDisk, characters);
        return characters;
    }


    public void setPointerToDisk(String pointerToDisk) {
        this.pointerToDisk = pointerToDisk;
        this.diskFile = pointerToDisk.split("_")[0];
        this.diskOffset = Integer.parseInt(pointerToDisk.split("_")[1]);
    }

    public Integer getDiskOffset() {
        return diskOffset;
    }
}
