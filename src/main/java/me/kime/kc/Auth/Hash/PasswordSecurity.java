/*
 * Copyright 2011 Sebastian Köhler <sebkoehler@whoami.org.uk>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.Kime.KC.Auth.Hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordSecurity {

    public static boolean comparePasswordWithHash(String password, String hash, String salt) throws NoSuchAlgorithmException {
    	MessageDigest m= MessageDigest.getInstance("MD5");
    	byte[] byteData = m.digest(password.getBytes());
    	StringBuffer sb = new StringBuffer();
    	for (int i = 0; i < byteData.length; i++) {
    		sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
    	}
    	String e1 = sb.append(salt).toString();
    	byteData = m.digest(e1.getBytes());
    	StringBuffer sb2 = new StringBuffer();
    	for (int i = 0; i < byteData.length; i++) {
    		sb2.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
    	}
    	
    	//debug
    	//System.out.println(sb2.toString());
    	//System.out.println(hash);
    	//System.out.println(salt);
    	
    	return hash.equalsIgnoreCase(sb2.toString());
    }
}
