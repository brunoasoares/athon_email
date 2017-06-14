
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;

/**
 * This class is used to receive email with attachment. * @author javawithease
 */

public class ReceiveEmailWithAttachment {

    private static String saveDirectory;

    private static void receiveEmail(String imapHost, String mailStoreType, String userName, String password) {
        //Set properties
        Properties props = new Properties();
        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.host", imapHost);
        props.put("mail.imap.port", "993");
        props.put("mail.imap.starttls.enable", "true");

        // Get the Session object.
        Session session = Session.getInstance(props);

        try {
            //Create the IMAP store object and connect to the pop store.
            Store store = session.getStore("imaps");
            store.connect(imapHost, userName, password);

            //Create the folder object and open it in your mailbox.
            Folder emailFolder = store.getDefaultFolder();
            for (Folder T:emailFolder.list()){
                for (Folder S:T.list()){
                    System.out.println(S.getName());
                }
            }
            emailFolder = emailFolder.getFolder("INBOX/ASG_NFE");
            emailFolder.open(Folder.READ_ONLY);

            //Retrieve the messages from the folder object.
            Message[] messages = emailFolder.getMessages();
            System.out.println("Total Message" + messages.length);

            //Iterate the messages
            for (Message message: messages) {
//                for (int i = 0; i < messages.length; i++) {
//                Message message = messages[i];
                Address[] toAddress = message.getRecipients(Message.RecipientType.TO);
                System.out.println("---------------------------------");
//                System.out.println("Details of Email Message " + (i + 1) + " :");
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);

                //Iterate recipients
                System.out.println("To: ");
                String contentType = message.getContentType();
                String messageContent = "";

                // store attachment file name, separated by comma
                String attachFiles = "";


                for (Address address: toAddress) {
//                    for (int j = 0; j < toAddress.length; j++) {
                    System.out.println(address.toString());
                }
                //Iterate multiparts

                if (contentType.contains("multipart")) {
                    // content may contain attachments
                    Multipart multiPart = (Multipart) message.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            if (part.getContentType().toLowerCase().contains("xml")) {
                                System.out.println("ExtensionType : " + part.getContentType());

                                // XML_treatment
                                String fileName = part.getFileName();
                                attachFiles += fileName + ", ";
                                part.saveFile(saveDirectory + File.separator + fileName);
                            }
                        } else {
                            //messageContent = part.getContent().toString();
                        }
                    }

                    if (attachFiles.length() > 1) {
                        attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                    }
                }
            }
        //close the folder and store objects
        emailFolder.close(false);
        store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String imapHost = "imap.gmail.com"; //change accordingly
        String mailStoreType = "pop3";
        final String userName = "athon@athonengenharia.com"; //change accordingly
        final String password = "karenbrunos2";//change accordingly
        setSaveDirectory("/Users/brunoandrade/Dropbox/Athon/Emails_NFe");
        // call receiveEmail
        receiveEmail(imapHost, mailStoreType, userName, password);
    }

    private static void setSaveDirectory(String dir) {
        saveDirectory = dir;
    }
}

