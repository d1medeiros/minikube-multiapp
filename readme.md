
```mermaid
flowchart LR
    gateway-->|find all| customer
    customer-->|return|gateway
```

```mermaid
flowchart LR
    gateway-->|find with customer id| account
    account-->|return account id|gateway
     gateway-->|find with account id| fraud
    fraud-->|return if is allowed|gateway
```

```mermaid
flowchart LR
    gateway-->|find with account id| offer
    offer-->|return offers|gateway
```

