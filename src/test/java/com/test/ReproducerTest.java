package com.test;

import com.antwerkz.bottlerocket.BottleRocket;
import com.antwerkz.bottlerocket.BottleRocketTest;
import com.github.zafarkhaja.semver.Version;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import dev.morphia.Datastore;
import dev.morphia.ModifyOptions;
import dev.morphia.Morphia;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import static com.mongodb.client.model.ReturnDocument.AFTER;
import static dev.morphia.query.experimental.updates.UpdateOperators.unset;

public class ReproducerTest extends BottleRocketTest {

    private Datastore datastore;

    public ReproducerTest() {
        MongoClient mongo = getMongoClient();
        MongoDatabase database = getDatabase();
        database.drop();
        datastore = Morphia.createDatastore(mongo, getDatabase().getName());
    }

    @NotNull
    @Override
    public String databaseName() {
        return "morphia_repro";
    }

    @Nullable
    @Override
    public Version version() {
        return BottleRocket.DEFAULT_VERSION;
    }

    @Test
    public void reproduce() {

        final var bar = datastore.save(new Bar());

        datastore
            .find(Bar.class)
            .modify(unset("foo"))
            .execute(new ModifyOptions().upsert(true).returnDocument(AFTER));

    }

}
