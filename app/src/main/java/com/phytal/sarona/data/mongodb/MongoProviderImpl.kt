package com.phytal.sarona.data.mongodb

import com.phytal.sarona.R
import com.phytal.sarona.data.Account
import com.phytal.sarona.data.provider.LoginInformation
import com.phytal.sarona.internal.helpers.SequenceGenerator
import kotlinx.coroutines.runBlocking
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

class MongoProviderImpl : MongoProvider {
    override fun getAccount(loginInformation: LoginInformation): Account? {
        val client = KMongo.createClient().coroutine //use coroutine extension
        val database = client.getDatabase("test") //normal java driver usage
        val col = database.getCollection<Account>() //KMongo extension method


        var user : Account? = null
//async now
        runBlocking {
            user = col.findOne(Account::username eq loginInformation.username, Account::password eq loginInformation.password, Account::link eq loginInformation.link)

            if (user == null) {
                val sequenceGenerator = SequenceGenerator()
                val id = sequenceGenerator.nextId()
                col.insertOne(Account(id, "test", "test", loginInformation.username, loginInformation.password, loginInformation.link, R.drawable.sarona_logo))
            }
            // create a user adn return it
            user = col.findOne(Account::username eq loginInformation.username, Account::password eq loginInformation.password, Account::link eq loginInformation.link)

        }
        return user
    }
}