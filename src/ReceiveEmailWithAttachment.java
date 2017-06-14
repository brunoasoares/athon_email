
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;

/**
 * This class is used to receive email with attachment. * @author javawithease
 */

public class ReceiveEmailWithAttachment {

    private static String saveDirectory;

    public static void receiveEmail(String pop3Host, String mailStoreType, String userName, String password) {
        //Set properties
        Properties props = new Properties();
        props.put("mail.store.protocol", "pop3");
        props.put("mail.pop3.host", pop3Host);
        props.put("mail.pop3.port", "995");
        props.put("mail.pop3.starttls.enable", "true");

        // Get the Session object.
        Session session = Session.getInstance(props);

        try {
            //Create the POP3 store object and connect to the pop store.
            Store store = session.getStore("pop3s");
            store.connect(pop3Host, userName, password);

            //Create the folder object and open it in your mailbox.
            Folder emailFolder = store.getDefaultFolder();
            emailFolder = emailFolder.getFolder("ASG_NFE");
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
                            messageContent = part.getContent().toString();
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
        String pop3Host = "pop.gmail.com"; //change accordingly
        String mailStoreType = "pop3";
        final String userName = "athon@athonengenharia.com"; //change accordingly
        final String password = "karenbrunos2";//change accordingly
        setSaveDirectory("/Users/brunoandrade/Dropbox/Athon/Emails_NFe");
        // call receiveEmail
        receiveEmail(pop3Host, mailStoreType, userName, password);
    }

    public static void setSaveDirectory(String dir) {
        saveDirectory = dir;
    }
}

