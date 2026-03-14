Jævnfør opgavebeskrivelsen er Dictionary<K, V> implementeret med en hash-tabel der bruger åben adressering og linear probing til kollisionshåndtering. Nøgle-værdi-par gemmes direkte i tabellen via en indre Entry<K, V>-klasse.

Designvalg
Negativ hash-kode: Håndteres med (key.hashCode() & Integer.MAX_VALUE) % capacity frem for Math.abs(), fordi Math.abs(Integer.MIN_VALUE) returnerer en negativ værdi i Java.
DELETED-sentinel: Ved sletning sættes pladsen til en særlig DELETED-markør i stedet for null. Det sikrer at probe-sekvenser ikke brydes, da null ellers ville stoppe en søgning for tidligt.
DELETED-pladser kan desuden genbruges ved efterfølgende indsætninger. Fuld tabel: Kaster en IllegalStateException. En produktionsimplementering ville i stedet foretage rehashing, men det er holdt ude af scope her.
