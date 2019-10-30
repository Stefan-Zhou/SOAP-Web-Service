package com.eShopool.backend;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;

import com.eShopool.backend.utils.RSAUtil;
import com.eShopool.backend.utils.DecodeTool;
import com.eShopool.backend.utils.Encoder;
import com.eShopool.backend.utils.AESUtil;
import com.eShopool.backend.jdbc.User;
import com.eShopool.backend.jdbc.Commodity;
import com.eShopool.backend.jdbc.Trade_info;
import com.eShopool.backend.log.JdbcException;
import com.eShopool.backend.log.LogRequest;

/**
 * The class provides all the web services dealing with the requests.
 * 
 * @author Group 10
 * @since 30/10/2019
 */
@WebService(serviceName = "SoapService")
public class SoapService {
    
    /**
     * When a new user gives his/her phone number, generate new key pair for this user.
     * Store the user's phone number, public key and private key into database.
     * Return the public key to the user.
     * 
     * @param encryptedPhone the encrypted phone number of user
     * @return the unique public key of the user
     */
    @WebMethod(operationName = "requestPublicKey")
    public String requestPublicKey(@WebParam(name = "arg0") String encryptedPhone) {
        try {
            //Decode the encrypted phone number
            String phoneNumber = DecodeTool.Base64Decoder(encryptedPhone);
            
            //Check whether the user already exists
            boolean userExist = User.check_u_phone(phoneNumber);
            String puk = "";
            if (userExist) {
                //If exist, return the stored public key
                puk = User.get_u_public(phoneNumber);
            }
            else {
                //If not exist, generate new key pair
                Map<String, String> keyMap = RSAUtil.generateKeyPair();
                puk = keyMap.get("publicKey");
                String prk = keyMap.get("privateKey");
                //Store the new user's information to the database
                User.create_account(phoneNumber, puk, prk);
            }
            return puk;
        } catch (Exception e) {
		e.printStackTrace();
                return "fail";
        }
    }
    
    /**
     * Decrypt the password and store it into database
     * 
     * @param publicKey the public key of the user
     * @param cryptograph the encrypted user password
     * @return the operation result signal
     */
    @WebMethod(operationName = "updateUserPwd")
    public String updateUserPwd(@WebParam(name = "arg0") String publicKey, @WebParam(name = "arg1") String cryptograph) {
        try {
            //Search the private key in the database using public key
            String privateKey = User.get_private_key(publicKey);
            //Decrpt the password using base64 and private key
            byte[] password = RSAUtil.decrypt(RSAUtil.getPrivatekey(privateKey),Base64.getDecoder().decode(cryptograph));
            String pwd = new String(password);
            //Store the password into the database
            int result = User.update_u_password(publicKey, pwd);
            if (result > 0)
                return "success";
            else
                return "fail";
        } catch (Exception ex) {
            Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex);
            return "fail";
        }
    }
    
    /**
     * Check whether the password is correct.
     * 
     * @param publicKey the public key of the user
     * @param cryptograph the encrypted user password
     * @return the check result of password
     */
    @WebMethod(operationName = "checkPwd")
    public String checkPwd(@WebParam(name = "arg0") String publicKey, @WebParam(name = "arg1") String cryptograph) {
        try {
            //Search the private key in the database using public key
            String privateKey = User.get_private_key(publicKey);
            //Decrpt the password using base64 and private key
            byte[] password = RSAUtil.decrypt(RSAUtil.getPrivatekey(privateKey),Base64.getDecoder().decode(cryptograph));
            String pwd = new String(password);
            //Get the correct password stored in the database
            String correctPwd = User.get_password(publicKey);
            //Check whether the password is correct
            if (correctPwd.equals(pwd))
                return "success";
            else
                return "fail";
        } catch (Exception ex) {
            Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex);
            return "fail";
        }
    }
    
    /**
     * Store the information of new item into database.
     * 
     * @param publicKey the public key of the user
     * @param itemName the name of the new item
     * @param itemLabel the label of the new item
     * @param itemPrice the price of the new item
     * @param itemAmount the amount of the new item
     * @param itemImagePath the image path of the new item
     * @param itemDesc the description of the new item
     * @return the result of the operation
     */
    @WebMethod(operationName = "addItem")
    public String addItem(@WebParam(name = "arg0") String publicKey, @WebParam(name = "arg1") String itemName, @WebParam(name = "arg2") String itemLabel, @WebParam(name = "arg3") String itemPrice, @WebParam(name = "arg4") String itemAmount, @WebParam(name = "arg5") String itemImagePath, @WebParam(name = "arg6") String itemDesc) {
        try {
            //Store the information of new item into database
            int result = Commodity.create_commodity(publicKey, itemName, itemPrice, itemAmount, itemLabel, itemDesc, itemImagePath);
            if (result > 0)
                return "success";
            else
                return "fail";
        } catch (SQLException ex) {
            Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex);
            return "fail";
        }
    }
    
    /**
     * Add money to the user's balance.
     * 
     * @param publicKey the public key of the user
     * @param money the amount of money going to be added
     * @return the result of the operation
     */
    @WebMethod(operationName = "updateUserBalance")
    public String updateUserBalance(@WebParam(name = "arg0") String publicKey, @WebParam(name = "arg1") String money) {
        try {
            //Add money to the user's balance
            double deposit = Double.parseDouble(money);
            int result = User.update_u_balance(publicKey, deposit);
            if (result > 0)
                return "success";
            else
                return "fail";
        } catch (SQLException ex) {
            Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex);
            return "fail";
        }
    }
    
    /**
     * Get the information of the commodity using its image path.
     * 
     * @param itemImagePath the image path of the commodity
     * @return the information of the commodity
     */
    @WebMethod(operationName = "getCommodityInfor")
    public String getCommodityInfor(@WebParam(name = "arg0") String itemImagePath) {
        try {
            //Get the information of the commodity using its image path
            String information = Commodity.get_commodity_infor(itemImagePath);
            return information;
        } catch (SQLException ex) {
            Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex);
            return "fail";
        }
    }
    
    /**
     * Check whether the user exists in the database using its phone number.
     * 
     * @param encryptedPhone the encrypted phone number
     * @return whether the user exists in the database
     */
    @WebMethod(operationName = "checkPhoneNumber")
    public String checkPhoneNumber(@WebParam(name = "arg0") String encryptedPhone) {
        try {
            //Decode the phone number using base64
            String phoneNumber = DecodeTool.Base64Decoder(encryptedPhone);
            
            //Check whether the user exists in the database
            boolean userExist = User.check_u_phone(phoneNumber);
            if (userExist)
                return "success";
            else
                return "fail";
        } catch (Exception e) {
		e.printStackTrace();
                return "fail";
        }
    }
    
    /**
     * Get the balance of the user using public key.
     * 
     * @param publicKey the public key of the user
     * @return the balance of the user
     */
    @WebMethod(operationName = "getBalance")
    public String getBalance(@WebParam(name = "arg0") String publicKey) {
        try {
            //Get the balance of the user using public key
            double balance = User.get_u_balance_by_public_key(publicKey);
            String returnBalance = String.valueOf(balance);
            //Encode the information and return
            return Encoder.Base64Encode(returnBalance);
        } catch (SQLException ex) {
            Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex);
            return "fail";
        }
    }
    
    /**
     * Implement the operation of buying items.
     * Check whether the buyer is the seller.
     * Check whether the item amount is enough.
     * Check whether the user balance is enough.
     * Add lock to the item during the operation.
     * Record the log if exception happens when the lock is not released.
     * Check the exception log and release the item lock at the beginning of the operation.
     * 
     * @param publicKey the public key of the user
     * @param itemAmount the amount of item the user want to buy
     * @param itemImagePath the image path of the item
     * @return the result of the operation
     */
    @WebMethod(operationName = "buyItem")
    public String buyItem(@WebParam(name = "arg0") String publicKey, @WebParam(name = "arg1") String itemAmount, @WebParam(name = "arg2") String itemImagePath) {
        try {
            //Read the exception log file
            String logImagePath1 = JdbcException.readException();
            //Release lock if exception happened last time
            if (!logImagePath1.equals("")) {
                Commodity.release_lock(logImagePath1);
                JdbcException.writeException("");
            }
            //Read the exception log file of another server
            String logImagePath2 = LogRequest.request();
            //Release lock if exception happened last time
            if (!(logImagePath2.equals("")||logImagePath2.equals("fail"))) {
                Commodity.release_lock(logImagePath2);
                JdbcException.writeException("");
            }
            
            //Check lock before buying the item
            if (Commodity.check_lock(itemImagePath).equals(1))
                return "System busy!";
            
            //Check whether the buyer is the seller
            String sellerPublicKey = Commodity.get_s_public_key(itemImagePath);
            if (sellerPublicKey.equals(publicKey)) {
                Commodity.release_lock(itemImagePath);
                return "You cannot buy your own products!";
            }
            
            //Check whether the products amount is enough
            int buyAmount = Integer.parseInt(itemAmount);
            int currentAmount = Commodity.get_c_num(itemImagePath);
            if (buyAmount > currentAmount) {
                Commodity.release_lock(itemImagePath);
                return "No enough products";
            }
            
            //Check whether the user balance is enough
            double currentBalance = User.get_u_balance_by_public_key(publicKey);
            double itemPrice = Commodity.get_c_price(itemImagePath);
            if (currentBalance < itemPrice*buyAmount) {
                Commodity.release_lock(itemImagePath);
                return "No enough money";
            }
            
            //Add lock to the item
            Commodity.add_lock(itemImagePath);
        } catch (Exception ex) {
            Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex);
            return "System busy!";
        }
        
        try {
            //Create new trade information in the database
            int result = Trade_info.create_trade(publicKey, itemImagePath, itemAmount);
            
            //Release lock after the purchase process
            Commodity.release_lock(itemImagePath);
            
            //Return result
            if (result > 0)
                return "success";
            else
                return "System busy!";
        } catch (SQLException ex) {
            Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex);
            try {
                //Write the exception into log file
                JdbcException.writeException(itemImagePath);
            } catch (IOException ex1) {
                Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return "System busy!";
        }
    }
    
    /**
     * Get the address of the user using public key.
     * 
     * @param publicKey the public key of the user
     * @return the address of the user
     */
    @WebMethod(operationName = "getAddress")
    public String getAddress(@WebParam(name = "arg0") String publicKey) {
        try {
            //Get the address of the user using public key
            String address = User.get_u_address_by_public_key(publicKey);
            //Encode the information and return
            return Encoder.Base64Encode(address);
        } catch (SQLException ex) {
            Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex);
            return "fail";
        }
    }
    
    /**
     * Get the buy bills information using public key.
     * 
     * @param publicKey the public key of the user
     * @return the buy bills information of the user
     */
    @WebMethod(operationName = "searchBuyBill")
    public String searchBuyBill(@WebParam(name = "arg0") String publicKey) {
        try {
            //Get the buy bills information using public key
            String info = Trade_info.get_trade_info_b(publicKey);
            //Encrypt information using phone number as AES key
            String phoneNumber = User.get_phone(publicKey);
            String encryptedInfo = AESUtil.encryptHex(info, phoneNumber);
            return encryptedInfo;
        } catch (SQLException ex) {
            Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex);
            return "fail";
        }
    }
    
    /**
     * Get the sell bills information using public key.
     * 
     * @param publicKey the public key of the user
     * @return the sell bills information of the user
     */
    @WebMethod(operationName = "searchSellBill")
    public String searchSellBill(@WebParam(name = "arg0") String publicKey) {
        try {
            //Get the sell bills information using public key
            String info =  Trade_info.get_trade_info_s(publicKey);
            //Encrypt information using phone number as AES key
            String phoneNumber = User.get_phone(publicKey);
            String encryptedInfo = AESUtil.encryptHex(info, phoneNumber);
            return encryptedInfo;
        } catch (SQLException ex) {
            Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex);
            return "fail";
        }
    }
    
    /**
     * Set the new address of the user into the database.
     * 
     * @param publicKey the public key of the user
     * @param encryptedAddress the encrypted address of the user
     * @return the result of the operation
     */
    @WebMethod(operationName = "setAddress")
    public String setAddress(@WebParam(name = "arg0") String publicKey, @WebParam(name = "arg1") String encryptedAddress) {
        try {
            //Get the private key of the user
            String privateKey = User.get_private_key(publicKey);
            //Decrypt the address using base64 and private key
            byte[] tempAddress = RSAUtil.decrypt(RSAUtil.getPrivatekey(privateKey),Base64.getDecoder().decode(encryptedAddress));
            String address = new String(tempAddress);
            //Store the address into the database
            int result = User.update_u_address(publicKey, address);
            if (result > 0)
                return "success";
            else
                return "fail";
        } catch (Exception ex) {
            Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex);
            return "fail";
        }
    }
    
    /**
     * Delete the account according to the user's phone number.
     * 
     * @param encryptedPhone the encrypted phone number of the user
     * @return the result of the operation
     */
    @WebMethod(operationName = "deleteAccount")
    public String deleteAccount(@WebParam(name = "arg0") String encryptedPhone) {
        try {
            //Decode the encrypted phone number
            String phoneNumber = DecodeTool.Base64Decoder(encryptedPhone);
            
            //Delete the account according to the user's phone number
            int result = User.drop_account(phoneNumber);
            if (result > 0)
                return "success";
            else
                return "fail";
        } catch (SQLException ex) {
            Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex);
            return "fail";
        }
    }
    
    /**
     * Read the local exception log file and return the content.
     * 
     * @return local xeception log content
     */
    @WebMethod(operationName = "readException")
    public String readException() {
        try {
            //Read the exception log file
            String log = JdbcException.readException();
            return log;
        } catch (IOException ex) {
            Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex);
            return "fail";
        }
    }
    
}
