package com.creatifsoftware.filonova.utils;

import android.net.Uri;

import com.creatifsoftware.filonova.BuildConfig;
import com.creatifsoftware.filonova.model.Equipment;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

import java.io.File;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Locale;


public class BlobStorageManager {
    /*
    **Only use Shared Key authentication for testing purposes!** 
    Your account name and account key, which give full read/write access to the associated Storage account, 
    will be distributed to every person that downloads your app. 
    This is **not** a good practice as you risk having your key compromised by untrusted clients. 
    Please consult following documents to understand and use Shared Access Signatures instead. 
    https://docs.microsoft.com/en-us/rest/api/storageservices/delegating-access-with-a-shared-access-signature 
    and https://docs.microsoft.com/en-us/azure/storage/common/storage-dotnet-shared-access-signature-part-1 
    */

    public static final String EQUIPMENT_CONTAINER_NAME = " equipments";
    public static final BlobStorageManager instance = new BlobStorageManager();
    static final String validChars = "abcdefghijklmnopqrstuvwxyz";
    private static final String testStorageConnectionString = "DefaultEndpointsProtocol=https;"
            + "AccountName=rentgostorage;"
            + "AccountKey=w+17Rp3M3WxTs+VAfDnMGkhYVZY/QwLaZCIEWKZtSU2Ld34yz1PCkuYtXqwowkXmAFBdr+x9KgqaWwz5DU6AMA==;"
            + "EndpointSuffix=core.windows.net";
    private static final String liveStorageConnectionString = "DefaultEndpointsProtocol=https;"
            + "AccountName=rentgostorage;"
            + "AccountKey=w+17Rp3M3WxTs+VAfDnMGkhYVZY/QwLaZCIEWKZtSU2Ld34yz1PCkuYtXqwowkXmAFBdr+x9KgqaWwz5DU6AMA==;"
            + "EndpointSuffix=core.windows.net";
    static SecureRandom rnd = new SecureRandom();

    static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(validChars.charAt(rnd.nextInt(validChars.length())));
        return sb.toString();
    }

    public String getBlobStorageUrl() {
        return "https://rentgostorage.blob.core.windows.net/";
    }

    private CloudBlobContainer getContainer(String containerName) throws Exception {
        // Retrieve storage account from connection-string.
//        String storageConnectionString = ApplicationUtils.instance.getLiveSwitchIsChecked(FilonovaServiceApp.getContext()) ?
//                                            ConnectionUtils.instance.getLiveBlobStorageConnectionString() :
//                                                ConnectionUtils.instance.getDevBlobStorageConnectionString();

        String storageConnectionString = BuildConfig.BLOB_CONNECTION_STRING;
        CloudStorageAccount storageAccount = CloudStorageAccount
                .parse(storageConnectionString);

        // Create the blob client.
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

        // Get a reference to a container.
        // The container name must be lower case
        return blobClient.getContainerReference(containerName);
    }

    public void UploadImage(String containerName, File file, String imageName) throws Exception {
        CloudBlobContainer container = getContainer(containerName);
        container.createIfNotExists();

        CloudBlockBlob imageBlob = container.getBlockBlobReference(imageName);
        imageBlob.getProperties().setContentType("jpeg");
        imageBlob.uploadFromFile(file.getPath());
    }

    public void deleteImage(String containerName, String imageName) throws Exception {
        CloudBlobContainer container = getContainer(containerName);
        container.createIfNotExists();
        CloudBlockBlob imageBlob = container.getBlockBlobReference(imageName);
        imageBlob.deleteIfExists();
    }

    public String prepareEquipmentImageName(Equipment selectedEquipment, String documentNumber, String type, String id) {
        return String.format(Locale.getDefault(), "%s/%s/%s/%s",
                selectedEquipment.plateNumber.replaceAll(" ", "").toLowerCase(),
                documentNumber.toLowerCase(),
                type.toLowerCase(),
                id.toLowerCase());
    }

    public String prepareCustomerDrivingLicenseImageName(String customerId, String name) {
        return String.format(Locale.getDefault(), "%s/%s",
                customerId.toLowerCase(),
                name.toLowerCase());
    }

    public String getEquipmentsContainerName() {
        return "equipments";
    }

    public String getCustomerContainerName() {
        return "customers";
    }

    public String[] ListImages(Equipment selectedEquipment, String documentNumber, String type) throws Exception {

        String containerName = String.format(Locale.getDefault(), "%s/%s/%s",
                selectedEquipment.plateNumber.replaceAll(" ", "").toLowerCase(),
                documentNumber.toLowerCase(),
                type.toLowerCase());

        CloudBlobContainer container = getContainer(containerName);

        Iterable<ListBlobItem> blobs = container.listBlobs();

        LinkedList<String> blobNames = new LinkedList<>();
        for (ListBlobItem blob : blobs) {
            blobNames.add(((CloudBlockBlob) blob).getName());
        }

        return blobNames.toArray(new String[blobNames.size()]);
    }

    public void GetImage(String containerName, String name, OutputStream imageStream, long imageLength) throws Exception {
        CloudBlobContainer container = getContainer(containerName);

        CloudBlockBlob blob = container.getBlockBlobReference(name);

        if (blob.exists()) {
            blob.downloadAttributes();

            blob.download(imageStream);
        }
    }

    public Uri GetImage(String containerName, String refName) throws Exception {
        CloudBlobContainer container = getContainer(containerName);

        CloudBlockBlob blob = container.getBlockBlobReference(refName);

        return Uri.parse(blob.getStorageUri().getPrimaryUri().toString());
    }

}
