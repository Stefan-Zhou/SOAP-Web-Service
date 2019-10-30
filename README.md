# SOAP-Web-Service Operations
## requestPublicKey
When a new user gives his/her phone number, generate new key pair for this user.
Store the user's phone number, public key and private key into database.
Return the public key to the user.

**Parameter**
- encryptedPhone: the encrypted phone number of user.

**Return**: the unique public key of the user.
## checkPhoneNumber
Check whether the user exists in the database using its phone number.

**Parameter**
- encryptedPhone: the encrypted phone number.

**Return**: whether the user exists in the database.
## checkPwd
Check whether the password is correct.

**Parameter**
- publicKey: the public key of the user.
- cryptograph: the encrypted user password.

**Return**: the check result of password.
## updateUserPwd
Decrypt the password and store it into database

**Parameter**
- publicKey: the public key of the user.
- cryptograph: the encrypted user password.

**Return**: the operation result signal.
## getBalance
Get the balance of the user using public key.

**Parameter**
- publicKey: the public key of the user.

**Return**: the balance of the user.
## updateUserBalance
Add money to the user's balance.

**Parameter**
- publicKey: the public key of the user.
- money: the amount of money going to be added.

**Return**: the result of the operation.
## getAddress
Get the address of the user using public key.

**Parameter**
- publicKey: the public key of the user.

**Return**: the address of the user.
## setAddress
Set the new address of the user into the database.

**Parameter**
- publicKey: the public key of the user.
- encryptedAddress: the encrypted address of the user.

**Return**: the result of the operation.
## deleteAccount
Delete the account according to the user's phone number.

**Parameter**
- encryptedPhone: the encrypted phone number of the user.

**Return**: the result of the operation.
## getCommodityInfor
Get the information of the commodity using its image path.

**Parameter**
- itemImagePath: the image path of the commodity.

**Return**: the information of the commodity.
## addItem
Store the information of new item into database.

**Parameter**
- publicKey: the public key of the user.
- itemName: the name of the new item.
- itemLabel: the label of the new item.
- itemPrice: the price of the new item.
- itemAmount: the amount of the new item.
- itemImagePath: the image path of the new item.
- itemDesc: the description of the new item.

**Return**: the result of the operation.
## buyItem
Implement the operation of buying items.
Check whether the buyer is the seller.
Check whether the item amount is enough.
Check whether the user balance is enough.
Add lock to the item during the operation.
Record the log if exception happens when the lock is not released.
Check the exception log and release the item lock at the beginning of the operation.

**Parameter**
- publicKey: the public key of the user.
- itemAmount: the amount of item the user want to buy.
- itemImagePath: the image path of the item.

**Return**: the result of the operation.
## searchBuyBill
Get the buy bills information using public key.

**Parameter**
- publicKey: the public key of the user.

**Return**: the buy bills information of the user.
## searchSellBill
Get the sell bills information using public key.

**Parameter**
- publicKey: the public key of the user.

**Return**: the sell bills information of the user.
## readException
Read the local exception log file and return the content.

**Return**: local xeception log content.

## The requested packages
Users need to import this packages to call functions.   

```Java
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
```  

## Set HTTP connection to SOAP web service
The parameter list:  
**opt:** The required operation names.  
**parameters:** The parameters passed to this operation.  

```Java
public class SoapRequest {

    /**
     * This method is used to set HTTP connection to SOAP web service.
     * @param opt operation name.
     * @param parameters parameters.
     * @return HTTP connection.
     */
    public static HttpURLConnection request(String opt, String ...parameters){
        String parameter_label = "";
        for(int i = 0;i<parameters.length;i++)
        {
            parameter_label +=  "<arg" + i + ">" + parameters[i] + "</arg" + i + ">";
        }

        try {
            // Web Service address.
            LoadSOAPBalance soap = new LoadSOAPBalance();
            String ip = "";
            while (ip.equals("")){
                ip = soap.getIP();
            }
            
            String bestURL = "http://" + ip + ":8080/SoapForeShopool-war/SoapService";
            URL url = new URL(bestURL);
            Log.e("balance", ip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            
            // Set the request header. (note that it must be in xml format)
            conn.setRequestProperty("content-type", "text/xml;charset=utf-8");
            
            // Construct the request body, in accordance with the SOAP specification.
            String requestBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                    + "<SOAP-ENV:Header/>"
                    + "<S:Body xmlns:ns2=\"http://backend.eShopool.com/\">"
                    + "<ns2:" + opt + ">"
                    + parameter_label
                    + "</ns2:" + opt + ">"
                    + "</S:Body>"
                    + "</S:Envelope>";
            System.out.println(requestBody);
            
            // Get a output stream.
            OutputStream out = conn.getOutputStream();
            out.write(requestBody.getBytes());
            out.close();
            return conn;
        } catch (Exception e) {
            return null;
        }
    }

}
```  
